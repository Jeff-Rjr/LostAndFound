# Lost & Found App

A simple Android application that allows users to report and browse lost and found items. Users can create a new advert by posting details and a photo of the item, browse all listings, view full item details, and remove items that have already been claimed.

---

## Features

- Post a lost or found item with a name, contact number, description, date, location, and photo
- Browse all listings in a scrollable list
- Search for items by name in real time
- Filter listings by Lost or Found
- View the full details of any listed item including the uploaded photo
- Remove an item from the list once it has been claimed

---

## Screens

**Landing Screen** — the entry point of the app with two options: create a new advert or browse all listings.

**Add Item Screen** — a form where users fill out the item details, pick a date, and upload a photo from their device gallery before saving the post.

**Item List Screen** — displays all posted items in a scrollable list with search and filter functionality.

**Item Detail Screen** — shows the complete information of a selected item with a Remove button at the bottom to delete the listing.

---

## Project Structure

```
app/src/main/
├── java/com/example/lostandfound/
│   ├── LandingActivity.java
│   ├── AddItemActivity.java
│   ├── ItemListActivity.java
│   ├── ItemDetailActivity.java
│   ├── database/
│   │   ├── LostFoundItem.java
│   │   └── DatabaseHelper.java
│   └── adapters/
│       └── ItemAdapter.java
└── res/
    └── layout/
        ├── activity_landing.xml
        ├── activity_add_item.xml
        ├── activity_item_list.xml
        ├── activity_item_detail.xml
        └── item_lost_found.xml
```

---

## Database

The app uses **SQLite** via Android's built-in `SQLiteOpenHelper` to store item data locally on the device. Data persists even after the app is closed or the device is restarted. The database supports inserting, reading with optional filtering, and deleting items.

---

## Built With

- Java
- Android Studio Panda 2
- SQLite via SQLiteOpenHelper
- RecyclerView
- ActivityResultLauncher for image picking
- Min SDK: API 24
