My Elasticsearch index to audit any type of event has the following mapping:

```json
{
  "mappings": {
    "dynamic": "false",
    "properties": {
      "_class": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "@timestamp": {
        "type": "date"
      },
      "data": {
        "type": "object"
      },
      "paths": {
        "type": "nested",
        "include_in_parent": true,
        "properties": {
          "key": {
            "type": "keyword"
          },
          "type": {
            "type": "keyword"
          },
          "text": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "number": {
            "type": "double"
          },
          "boolean": {
            "type": "boolean"
          },
          "date": {
            "type": "date"
          },
          "array_length": {
            "type": "integer"
          },
          "array_key": {
            "type": "keyword"
          },
          "array_index": {
            "type": "integer"
          }
        }
      }
    }
  }
}
```

This is a sample event representing an ecommerce order:

```json
{
  "id": "ad3cc276-025e-449c-9fcf-612594f83a77",
  "type": "ORDER",
  "data": {
    "orderId": "b526ce99-a50c-4199-b684-d962e7422adb",
    "totalPrice": 2399.95,
    "shippingAddress": {
      "zip": "90867",
      "country": {
        "code": "US",
        "name": "United States"
      },
      "city": "Houston",
      "street": "192 Oak St",
      "state": "TX"
    },
    "billingAddress": {
      "zip": "38013",
      "country": {
        "code": "US",
        "name": "United States"
      },
      "city": "New York",
      "street": "372 Main St",
      "state": "NY"
    },
    "items": [
      {
        "unitPrice": 899.99,
        "quantity": 2,
        "productId": "PHN456",
        "productName": "Smartphone Pro"
      },
      {
        "unitPrice": 199.99,
        "quantity": 3,
        "productId": "HDP789",
        "productName": "Wireless Headphones"
      }
    ],
    "orderDate": "2025-03-15T11:52:17.259256900Z",
    "customer": {
      "phone": "123-456-7890",
      "name": "Alice Smith",
      "email": "alice.smith@example.com"
    },
    "status": "NEW"
  },
  "paths": [
    {
      "key": "orderId",
      "type": "text",
      "text": "b526ce99-a50c-4199-b684-d962e7422adb"
    },
    {
      "key": "totalPrice",
      "type": "number",
      "text": "2399.95",
      "number": 2399.95
    },
    {
      "key": "shippingAddress.zip",
      "type": "text",
      "text": "90867"
    },
    {
      "key": "shippingAddress.country.code",
      "type": "text",
      "text": "US"
    },
    {
      "key": "shippingAddress.country.name",
      "type": "text",
      "text": "United States"
    },
    {
      "key": "shippingAddress.city",
      "type": "text",
      "text": "Houston"
    },
    {
      "key": "shippingAddress.street",
      "type": "text",
      "text": "192 Oak St"
    },
    {
      "key": "shippingAddress.state",
      "type": "text",
      "text": "TX"
    },
    {
      "key": "billingAddress.zip",
      "type": "text",
      "text": "38013"
    },
    {
      "key": "billingAddress.country.code",
      "type": "text",
      "text": "US"
    },
    {
      "key": "billingAddress.country.name",
      "type": "text",
      "text": "United States"
    },
    {
      "key": "billingAddress.city",
      "type": "text",
      "text": "New York"
    },
    {
      "key": "billingAddress.street",
      "type": "text",
      "text": "372 Main St"
    },
    {
      "key": "billingAddress.state",
      "type": "text",
      "text": "NY"
    },
    {
      "key": "items[0].unitPrice",
      "type": "number",
      "text": "899.99",
      "number": 899.99
    },
    {
      "key": "items[0].quantity",
      "type": "number",
      "text": "2",
      "number": 2
    },
    {
      "key": "items[0].productId",
      "type": "text",
      "text": "PHN456"
    },
    {
      "key": "items[0].productName",
      "type": "text",
      "text": "Smartphone Pro"
    },
    {
      "key": "items[1].unitPrice",
      "type": "number",
      "text": "199.99",
      "number": 199.99
    },
    {
      "key": "items[1].quantity",
      "type": "number",
      "text": "3",
      "number": 3
    },
    {
      "key": "items[1].productId",
      "type": "text",
      "text": "HDP789"
    },
    {
      "key": "items[1].productName",
      "type": "text",
      "text": "Wireless Headphones"
    },
    {
      "key": "orderDate",
      "type": "date",
      "text": "2025-03-15T11:52:17.259256900Z",
      "date": "2025-03-15T11:52:17.2592569Z"
    },
    {
      "key": "customer.phone",
      "type": "text",
      "text": "123-456-7890"
    },
    {
      "key": "customer.name",
      "type": "text",
      "text": "Alice Smith"
    },
    {
      "key": "customer.email",
      "type": "text",
      "text": "alice.smith@example.com"
    },
    {
      "key": "status",
      "type": "text",
      "text": "NEW"
    }
  ],
  "@timestamp": "2025-03-15T11:52:17.258256Z"
}
```

The nested paths property is actually the flattening of the un-indexed data JSON object property as JSONPATH-like dot-delimited keys and value pairs.

I need an Elasticsearch aggregation search where:

- I can group and the orders by customer email (the customer email is available at the nested path with key `customer.email` and text representing the actual email value)
- There is a minimum of two orders per customer

