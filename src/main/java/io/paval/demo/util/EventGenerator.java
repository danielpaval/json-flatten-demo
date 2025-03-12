package io.paval.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.paval.demo.dto.EventDto;
import io.paval.demo.dto.EventType;
import io.paval.demo.impl.elasticsearch.ElasticsearchEvent;
import io.paval.demo.impl.elasticsearch.ElasticsearchEventMapper;
import io.paval.demo.impl.elasticsearch.ElasticsearchEventMapperImpl;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@UtilityClass
public class EventGenerator {

    private static final List<Customer> CUSTOMERS = List.of(
            new Customer("john.doe@example.com", "John Doe"),
            new Customer("jane.doe@example.com", "Jane Doe"),
            new Customer("alice.smith@example.com", "Alice Smith"),
            new Customer("bob.jones@example.com", "Bob Jones"),
            new Customer("carol.white@example.com", "Carol White")
    );

    private static final List<String> STATUSES = List.of("NEW", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");

    private static final List<Item> ITEMS = List.of(
            new Item("LAP123", "Gaming Laptop", 1299.99),
            new Item("PHN456", "Smartphone Pro", 899.99),
            new Item("HDP789", "Wireless Headphones", 199.99),
            new Item("MON321", "4K Monitor", 499.99),
            new Item("KBD654", "Mechanical Keyboard", 149.99)
    );
    private static final List<String> CITIES = List.of("New York", "Los Angeles", "Chicago", "Houston", "Phoenix");
    private static final List<String> STATES = List.of("NY", "CA", "IL", "TX", "AZ");

    private static final Random random = new Random();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final ElasticsearchEventMapper ELASTICSEARCH_EVENT_MAPPER = new ElasticsearchEventMapperImpl();

    private static final Integer BATCH_SIZE = 100000;

    public static void main(String[] args) {
        generate(1);
    }

    public void generate(Integer count) {
        generate(count, Format.ELASTICSEARCH);
    }

    public static void generate(Integer count, Format format) {
        LocalDateTime start = LocalDateTime.now();
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        log.info("Batch 0");
        for (int i = 0; i < count % BATCH_SIZE; i++) {
            executor.submit(() -> {
                generate(Path.of("events-%d-%d.json".formatted(count >= BATCH_SIZE ? BATCH_SIZE : count, 0)), format, 1);
            });
        }
        for (int i = 0; i < count / BATCH_SIZE; i++) {
            int batch = i + 1;
            executor.submit(() -> {
                log.info("Batch {}", batch);
                generate(Path.of("events-%d-%d.json".formatted(count >= BATCH_SIZE ? BATCH_SIZE : count, batch)), format, BATCH_SIZE);
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
            log.info("Finished in {}", Duration.between(start, LocalDateTime.now()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void generate(Path path, Format format, Integer count) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            for (int i = 0; i < count; i++) {
                if (Format.ELASTICSEARCH.equals(format)) {
                    EventDto eventDto = generate();
                    ElasticsearchEvent elasticsearchEvent = ELASTICSEARCH_EVENT_MAPPER.eventDtoToEvent(eventDto);
                    writer.write(OBJECT_MAPPER.writeValueAsString(elasticsearchEvent));
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (Exception e) {
            log.error("Failed to generate to path {}", path, e);
        }
    }

    public static EventDto generate() {
        EventDto eventDto = new EventDto();
        eventDto.setType(EventType.ORDER);
        eventDto.setId(UUID.randomUUID());
        eventDto.setDate(ZonedDateTime.now(ZoneOffset.UTC));
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", UUID.randomUUID());
        Map<String, Object> customer = generateCustomer();
        data.put("customer", customer);
        List<Map<String, Object>> items = generateItems();
        data.put("items", items);
        data.put("shippingAddress", createAddress());
        data.put("billingAddress", createAddress());
        data.put("orderDate", ZonedDateTime.now(ZoneOffset.UTC).toString());
        data.put("status", STATUSES.get(random.nextInt(STATUSES.size())));
        data.put("totalPrice", getItemsTotal(items));
        eventDto.setData(data);
        return eventDto;
    }

    private static Map<String, Object> generateCustomer() {
        Map<String, Object> customerMap = new HashMap<>();
        Customer customer = CUSTOMERS.get(random.nextInt(5));
        customerMap.put("name", customer.name());
        customerMap.put("email", customer.email());
        customerMap.put("phone", "123-456-7890");
        return customerMap;
    }
    
    private static List<Map<String, Object>> generateItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        int itemCount = random.nextInt(1, 5);
        for (int i = 0; i < itemCount; i++) {
            Item item = ITEMS.get(random.nextInt(ITEMS.size()));
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productId", item.id());
            itemMap.put("productName", item.name());
            itemMap.put("quantity", random.nextInt(1, 4));
            itemMap.put("unitPrice", item.price());
            items.add(itemMap);
        }
        return items;
    }

    private Map<String, Object> createAddress() {
        int index = random.nextInt(CITIES.size());
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("street", random.nextInt(1, 999) + " " +
                (random.nextBoolean() ? "Main" : "Oak") + " St");
        addressMap.put("city", CITIES.get(index));
        addressMap.put("state", STATES.get(index));
        addressMap.put("zip", String.format("%05d", random.nextInt(100000)));
        Map<String, String> country = new HashMap<>();
        country.put("name", "United States");
        country.put("code", "US");
        addressMap.put("country", country);
        return addressMap;
    }

    private BigDecimal getItemsTotal(List<Map<String, Object>> items) {
        return items.stream()
                .map(item -> BigDecimal.valueOf((Double) item.get("unitPrice"))
                        .multiply(BigDecimal.valueOf((Integer) item.get("quantity"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    record Item(String id, String name, double price) {}

    record Customer(String email, String name) {}

    enum Format {

        ELASTICSEARCH, // JSON
        JPA // CSV

    }

}
