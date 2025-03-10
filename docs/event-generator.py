import csv
from faker import Faker
import random

fake = Faker()

def generate_csv(file_name, num_entries):
    with open(file_name, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['orderId', 'customer_name', 'customer_email', 'customer_phone', 'productId', 'productName', 'quantity', 'price', 'street', 'city', 'state', 'zip', 'country', 'orderDate', 'status', 'totalAmount'])

        for _ in range(num_entries):
            writer.writerow([
                fake.uuid4(),
                fake.name(),
                fake.email(),
                fake.phone_number(),
                fake.uuid4(),
                fake.word(),
                random.randint(1, 10),
                round(random.uniform(10.0, 100.0), 2),
                fake.street_address(),
                fake.city(),
                fake.state_abbr(),
                fake.zipcode(),
                'United States',
                fake.date_time_this_year().isoformat(),
                'Processing',
                round(random.uniform(50.0, 500.0), 2)
            ])

if __name__ == "__main__":
    generate_csv('events.csv', 1)