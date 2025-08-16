# Generic Model Service

Generic Model Service provides REST APIs to persist **generic models** that are commonly required in backend applications.  
Instead of creating a separate table for each dataset, this service offers a **unified approach** with a single table at the backend, designed to handle different types of generic data.

---

## ✨ Features
- Unified **Generic Model** table for storing lookup-style data.
- **Enum-based generic types** (see: `io.github.sivaramanr.common.types.GenericType`).
- **Translation support** for `name`, `value`, and `description` fields across multiple locales.
- Pre-configured with **H2 database** for local development.
- Tested with **MySQL** for production usage.

---

## 📥 Download & Run (Quick Start)

You can try it immediately without any setup.

1. Go to the [**Releases**](../../releases) page.
2. Download the latest JAR file:
   ```
   generic-model-service-x.y.z.jar
   ```  
3. Run it with H2 (in-memory DB):
   ```bash
   java -jar generic-model-service-x.y.z.jar
   ```  
4. Open [http://localhost:8080](http://localhost:8080)

That’s it 🚀

---

## 🛠 Example Usage

### Create a Generic Model
```http
POST /genericmodel
Headers:
  tenantId: 1000
  username: testuser

Body:
{
  "genericType": "PRODUCT_CATEGORY",
  "name": "Electronics",
  "valueType": "STRING",
  "sortOrder": 10,
  "value": "ELECT",
  "status": "ACTIVE",
  "description": "Product category for electronics"
}
```

### Get All Models by Type
```http
GET /genericmodel?genericType=PRODUCT_CATEGORY
Headers:
  tenantId: 1000
```

---

## 📦 Bulk Import
- No dedicated API for bulk creation.
- Bulk data can be directly imported into the database.

---

## ⚡ Production Notes
- Since all generic data is stored in a single table, it is recommended to **sync the database table with an in-memory cache** (e.g., Redis or Memcached).
- Other services can then fetch data at **low latency** without querying the database repeatedly.

---

## 🔄 Use Cases
This service is best suited as a replacement for **lookup tables**, for example:
- Product categories
- Status types
- Value types
- Reference data

---

## 📚 Notes
- Ensure the `GenericType` enum value exists in `io.github.sivaramanr.common.types.GenericType` before creating models.
- Translations can be added for text fields (`name`, `value`, `description`) in multiple locales.

---

## 🏗 Roadmap
- ✅ Basic CRUD APIs
- 🔲 Bulk API for import/export
- 🔲 Admin UI for managing lookup data

---

## 📜 License
MIT License.  
