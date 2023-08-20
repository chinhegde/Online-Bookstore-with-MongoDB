# Online-Bookstore-with-MongoDB
Exploration of MongoDB as a part of CS 157C Introduction to NoSQL databases

## Data Model Explanation:
Data model can be found in _amazon_data_model.txt_

### Author collection:
I have added Author ID to collection which acts as a unique identifier since multiple authors can have the same name.

### Customer collection:
The following conditions were considered:
- A customer can have multiple addresses
- A customer can have multiple orders of books - this condition is used in the Orders collection (reduces redundancy in Customer collection)
- I have added the username as Customer ID because it is usually unique to a user and also added customer’s name as an attribute.

### Order collection:
I have added Order ID to the collection to uniquely identify each order and also Customer ID. This enables us to retrieve all orders 1 user. If this was data (orders) was in the Customer collection, this would increase the redundancy and as an admin, it would made it difficult to calculate transaction/order level data such as sales.
- Additionally, I have added “quantity” and “price” for each Book attribute for the same reason - also enabling an order to have the same book multiple times and multiple books as well.

### Book collection:
The following conditions were considered:
- A book can have multiple publishers
- Publishers can have multiple addresses
- A book can have many customer reviews
- A book can belong to multiple categories
- Additionally, I have created the data model in a way that books can have multiple authors as well (as this is prominent in academia). Since ISBN is unique for each book, instead of having a “Book ID” attribute, this data model uses ISBN as the ID.
- The “date” attribute has been used outside of “Publisher” data because publishers would not usually have a date (the established date of publisher does not make sense in the Book Management system world).
- In the “Reviews” attribute, I have used Customer ID (which would be the username) to identify the Customer in the review. Multiple reviews are allowed.

## Functional Requirements

The following requirements were executed:
1. View books
2. View authors 
3. View customers
4. Insert a book (check for duplication)
5. Insert a user (check for duplication, ability to add many addresses
6. Search for a book by title
7. Search books by author name
8. View Customer Information by creation date
9. View books with multiple publishers
10. View book reviews
11. Update book information
12. View database details
