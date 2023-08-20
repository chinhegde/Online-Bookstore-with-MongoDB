package com.check.verify;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.util.JSON; 
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.eq; 
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Date;


public class App 
{
    public static void main( String[] args ) 
    {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Hegde_AmazonDB");

		MongoCollection<Document> hegde_author = database.getCollection("AUTHOR");
		MongoCollection<Document> hegde_cust = database.getCollection("CUSTOMER");
		MongoCollection<Document> hegde_order = database.getCollection("ORDER");
		MongoCollection<Document> hegde_book = database.getCollection("BOOK");

        // Multiple insertion - AUTHOR
        List<Document> authors = new ArrayList<>();
        authors.add(new Document("_id", "abrahams!lberschatz")
                .append("first_name", "Abraham")
                .append("last_name", "Silberschatz")
                .append("country", "USA"));
        
        authors.add(new Document("_id", "greggagne")
                .append("first_name", "Greg")
                .append("last_name", "Gagne")
                .append("country", "USA"));
        hegde_author.insertMany(authors);

        // single insertion - CUSTOMER

        // single insertion - BOOK
        Document book = new Document()
                .append("title", "Introduction to MongoDB")
                .append("_id", "9781308129388")
                .append("authors", Arrays.asList("Kristina Chodorow", "Michael Dirolf"))
                .append("date", "2009-11-05")
                .append("publishers", Arrays.asList(
                        new Document()
                                .append("name", "John Wiley & Sons")
                                .append("address", Arrays.asList(
                                        new Document()
                                                .append("street", "111 River Street")
                                                .append("city", "Hoboken")
                                                .append("zip", "07030")
                                                .append("state", "NJ")
                                                .append("country", "USA")
                                )),
                        new Document()
                                .append("name", "Scholastic Press")
                                .append("address", Arrays.asList(
                                        new Document()
                                                .append("street", "10th St")
                                                .append("city", "San Jose")
                                                .append("zip", "95126")
                                                .append("state", "CA")
                                                .append("country", "USA")
                                ))
                ))
                .append("available", true)
                .append("pages", 976)
                .append("summary", "This book provides a comprehensive introduction to the fundamental concepts of MongoDB")
                .append("categories", Arrays.asList("Computers", "MongoDB", "NoSQL"))
                .append("language", "English");
        hegde_book.insertOne(book);

        // single insertion - ORDER
        Document orderExample = new Document("_id", "ord123")
                .append("order_date", "2023-04-22")
                .append("delivery_date", "2023-05-01")
                .append("customer_id", "chinmayihegde")
                .append("books", Arrays.asList(
                        new Document("book_id", "9780812993547")
                                .append("quantity", 1)
                                .append("price", 15.99)
                ));

        orderCollection.insertOne(orderExample);

        // Inserting into database with JSON File - AUTHOR
        List<InsertOneModel<Document>> insertauth = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("author.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Document authDoc = Document.parse(line);
                insertauth.add(new InsertOneModel<>(authDoc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BulkWriteResult result = hegde_author.bulkWrite(insertauth);
        System.out.println("Inserted " + result.getInsertedCount() + " documents into AUTHOR.");

        // Inserting into database with JSON File - CUSTOMER
        List<InsertOneModel<Document>> insertcust = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customer.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Document custDoc = Document.parse(line);
                insertcust.add(new InsertOneModel<>(custDoc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BulkWriteResult result = hegde_cust.bulkWrite(insertcust);
        System.out.println("Inserted " + result.getInsertedCount() + " documents into CUSTOMER.");

        // Inserting into database with JSON File - ORDER
        List<InsertOneModel<Document>> insertord = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("order.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Document ordDoc = Document.parse(line);
                insertord.add(new InsertOneModel<>(ordDoc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BulkWriteResult result = hegde_order.bulkWrite(insertord);
        System.out.println("Inserted " + result.getInsertedCount() + " documents into ORDER.");

        // Inserting into database with JSON File - BOOK
        List<InsertOneModel<Document>> insertbooks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("books.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Document bookDoc = Document.parse(line);
                insertbooks.add(new InsertOneModel<>(bookDoc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BulkWriteResult result = hegde_book.bulkWrite(insertbooks);
        System.out.println("Inserted " + result.getInsertedCount() + " documents into BOOKS.");

    	Scanner sc = new Scanner(System.in);

        Map<Integer, String> menu = new HashMap<>();
        menu.put(1, "View books");
        menu.put(2, "View authors");
        menu.put(3, "View Customers");
        menu.put(4, "Insert a book");
        menu.put(5, "Insert a user");
        menu.put(6, "Search for a book by title");
        menu.put(7, "Search books by author name");
        menu.put(8, "View Customer information by creation date");
        menu.put(9, "View books with multiple publishers");
        menu.put(10, "View books with reviews");
        menu.put(11, "Update book information");
        menu.put(12, "View database details");
        menu.put(13, "Exit");

        while (true) {
			
            System.out.println("Menu:");
            for (Map.Entry<Integer, String> entry : menu.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }

            System.out.print("\nMake a choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewBooks(hegde_book);
                    break;
                case 2:
                    viewAuthors(hegde_author);
                    break;
                case 3:
                    viewCustomers(hegde_cust);
                    break;
                case 4:
                    insertBook(hegde_book);
                    break;
                case 5:
                    insertUser(hegde_cust);
                    break;
                case 6:
                    searchBookTitle(hegde_book);
                    break;
                case 7:
                    searchBookAuthor(hegde_book);
                    break;
                case 8:
					FindIterable<Document> iterable = hegde_cust.find(eq("addresses.city", "San Jose"));
                    System.out.println("--- Users from San Jose ---");
                    for (Document doc : iterable) {
                        System.out.println(doc.getString("_id") + " " + doc.getString("name"));
                    }

                    // Find users created after 18th April 2020
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date createdAfter = null;
                    try {
                        createdAfter = format.parse("2020-04-18");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n--- Users created AFTER the given date - 18th April 2020) ---");
                    iterable = hegde_cust.find(gt("date_created", createdAfter));
                    for (Document doc : iterable) {
                        String dateCreated = doc.getString("date_created");
                        Date created = null;
                        try {
                            created = format.parse(dateCreated);
                        } catch (ParseException e) {
                            continue;
                        }
                        if (created.after(createdAfter)) {
                            System.out.println(">>>" + doc.getString("_id") + " was created on " + dateCreated);
                        }
                    }
                    break;
                case 9:
                    List<Document> res = hegde_book.find().into(new ArrayList<Document>());
					for (Document i : res) {
						List<Document> publishers = i.getList("publishers", Document.class);
						if (publishers != null && publishers.size() > 1) {
							System.out.println(i.getString("title") + " by " + i.getList("authors", String.class));
						}
					}
                    break;
                case 10:
                    res = hegde_book.find(exists("reviews")).into(new ArrayList<Document>());
					for (Document i : res) {
						System.out.println(i.getString("title") + " by " + i.getList("authors", String.class));
					}
                    break;
                case 11:
                    updateBook(hegde_book);
                    break;
                case 12:
                    dbDetails(database);
                    break;
                case 13:
                    System.out.println("Bye!");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
	}

	public void dbDetails(MongoDatabase database) {
		MongoIterable<String> collectionNames = database.listCollectionNames();
		for (String collectionName : collectionNames) {
			System.out.println(collectionName);
		}
	}

	public static void updateBook(MongoCollection<Document> hegde_book) {
		Scanner input = new Scanner(System.in);
		System.out.print("Which book details do you want to update? Enter ISBN: ");
		String bid = input.nextLine();

		Document res = book.find(eq("_id", bid)).first();
		if (res == null) {
			System.out.println("Book does not exist");
			return;
		}

		System.out.println("\n---- BEFORE UPDATE ----");
		System.out.println(res.toJson());

		System.out.print("What attribute do you want to update? ");
		String attr = input.nextLine();

		if (attr.equals("_id")) {
			System.out.println("Cannot update ISBN! No changes will be made");
		} else {
			System.out.print("Enter new " + attr + ": ");
			String newValue = input.nextLine();
			res.put(attr, newValue);
			book.replaceOne(eq("_id", bid), res);
		}

		System.out.println("\n---- AFTER UPDATE ----");
		System.out.println(res.toJson());
	}

	private static void viewBooks(MongoCollection<Document> hegde_book) {
        System.out.println("The number of books in the database are: " + hegde_book.countDocuments());
        System.out.println("\nThe books are: ");
        for (Document doc : hegde_book.find()) {
            System.out.println(doc);
        }
    }

	public static void searchBookAuthor(MongoCollection<Document> hegde_book) {
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        List<Document> books = hegde_book.find().into(new ArrayList<>());
        for (Document book : books) {
            if (book.getString("author").contains(author)) {
                System.out.println(book.toJson());
            }
        }
	}

	public void searchBookByTitle(MongoCollection<Document> hegde_book) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter title of the book: ");
		String title = scanner.nextLine();
		
		FindIterable<Document> result = book.find(new Document("title", title));
		
		for (Document document : result) {
			System.out.println(document.toJson());
    	}

	}

	 public static void insertUser(MongoCollection<Document> hegde_cust) {
        Scanner input = new Scanner(System.in);

        System.out.print("Customer name: ");
        String cname = input.nextLine();

        String uname;
        while (true) {
            System.out.print("Username (must be unique): ");
            uname = input.nextLine();
            if (cust.find(new Document("_id", uname)).first() != null) {
                System.out.println("This user already exists in the database!");
            } else {
                break;
            }
        }

        System.out.print("Password: ");
        String pwd = String.valueOf(input.nextLine().hashCode());

        String active = "Y";

        System.out.print("How many addresses does the user have? ");
        int numOfAddresses = Integer.parseInt(input.nextLine());

        ArrayList<Document> addresses = new ArrayList<>();
        for (int i = 0; i < numOfAddresses; i++) {
            System.out.print("Street: ");
            String street = input.nextLine();

            System.out.print("City: ");
            String city = input.nextLine();

            System.out.print("ZIP Code: ");
            String zip = input.nextLine();

            System.out.print("State: ");
            String state = input.nextLine();

            System.out.print("Country: ");
            String country = input.nextLine();

            Document address = new Document("STREET", street)
                    .append("CITY", city)
                    .append("ZIP", zip)
                    .append("STATE", state)
                    .append("COUNTRY", country);

            addresses.add(address);
        }

        LocalDate doc = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateOfCreation = doc.format(formatter);

        Document customer = new Document("CUSTOMER", cname)
                .append("_id", uname)
                .append("PASSWORD", pwd)
                .append("ACTIVE", active)
                .append("ADDRESS", addresses)
                .append("DATE_OF_CREATION", dateOfCreation);

        cust.insertOne(customer);

        System.out.println(cname.toUpperCase() + " has been inserted.");

        input.close();
    }

	public static void viewCustomers(MongoCollection<Document> hegde_cust) {
		System.out.println("The number of customers in the database are: " + cust.countDocuments());
		System.out.println("\nThe customers are: ");

		FindIterable<Document> customers = cust.find();
		int i = 1;
		for (Document customer : customers) {
			System.out.println(i + ". " + customer.getString("USERNAME"));
			i++;
		}
	}

	public void viewAuthors(MongoCollection<Document> hegde_author) {
		System.out.println("The number of authors in the database are: " + hegde_author.countDocuments());
		System.out.println("\nThe authors are: ");

		int i = 1;
		for (Document doc : hegde_author.find()) {
			System.out.println(i + ". " + doc.getString("FIRST_NAME") + " " + doc.getString("LAST_NAME"));
			i++;
		}
	}

	public void viewBooks(MongoCollection<Document> hegde_book) {
        System.out.println("The number of books in the database are: " + bookCollection.countDocuments());
        System.out.println("\nThe books are: ");

        // retrieve all books and print their titles and authors
        MongoCursor<Document> cursor = bookCollection.find().iterator();
        int i = 1;
        while (cursor.hasNext()) {
            Document book = cursor.next();
            System.out.println(i++ + ". " + book.getString("TITLE") + " by " + book.get("AUTHOR"));
        }
    }

	public static void insertBook(MongoCollection<Document> hegde_book) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        System.out.print("Title of the book: ");
        String title = sc.nextLine();
        System.out.print("Author (separate with comma if there are many): ");
        String[] authors = sc.nextLine().split(",");
        System.out.print("ISBN of the book: ");
        String isbn = sc.nextLine();
        
        // Check if the book exists in the database
        // Assuming book is a MongoCollection object
         if(book.findOne(new BasicDBObject("_id", isbn)) != null) {
			System.out.println("This book already exists in the database!");
			return;
		}
        
        System.out.print("Published date (YYYY/MM/YY): ");
        String pdateStr = sc.nextLine();
        Date pdate = dateFormat.parse(pdateStr);
        
        System.out.print("Do you know publisher details? (Y/N) ");
        String if_pub = sc.nextLine();
        
        if (if_pub.equalsIgnoreCase("Y")) {
            System.out.print("Publisher name: ");
            String pname = sc.nextLine();
            System.out.println("Address is required: ");
            System.out.print("Street: ");
            String pst = sc.nextLine();
            System.out.print("City: ");
            String pc = sc.nextLine();
            System.out.print("State: ");
            String pstate = sc.nextLine();
            System.out.print("ZIP Code: ");
            String pzip = sc.nextLine();
            System.out.print("Country: ");
            String pco = sc.nextLine();
            
            ArrayList<Document> publisherList = new ArrayList<Document>();
            Document publisher = new Document();
            publisher.append("NAME", pname);
            
            ArrayList<Document> addressList = new ArrayList<Document>();
            Document address = new Document();
            address.append("STREET", pst);
            address.append("CITY", pc);
            address.append("STATE", pstate);
            address.append("ZIP", pzip);
            address.append("COUNTRY", pco);
            addressList.add(address);
            
            publisher.append("ADDRESS", addressList);
            publisherList.add(publisher);
        }
        
        System.out.print("Is it currently available? (Y/N)");
        String avail = sc.nextLine();
        System.out.print("Number of pages: ");
        int pages = Integer.parseInt(sc.nextLine());
        System.out.print("Summary of the book: ");
        String summary = sc.nextLine();
        System.out.print("Categories: ");
        String subs = sc.nextLine();
        System.out.print("Language: ");
        String lang = sc.nextLine();
        System.out.print("Are there any reviews? (Y/N)");
        String is_rev = sc.nextLine();
        
        BasicDBObject review = new BasicDBObject();
		if(is_rev.toUpperCase().equals("Y")) {
			System.out.print("Customer Name: ");
			String cus = scanner.nextLine();

			System.out.print("What is the review? ");
			String rev = scanner.nextLine();

			review.put("CUSTOMER", cus);
			review.put("REVIEW_BODY", rev);
		}
        
        BasicDBObject bookItem = new BasicDBObject();
        book_item.append("TITLE", title);
        book_item.append("AUTHOR", Arrays.asList(authors));
        book_item.append("_id", isbn);
		bookItem.put("DATE", pdate);
		bookItem.put("AVAILABLE", avail);
		bookItem.put("PAGES", pages);
		bookItem.put("SUMMARY", summary);
		bookItem.put("CATEGORIES", Arrays.asList(subs));
		bookItem.put("LANGUAGE", lang);

		if (if_pub.equalsIgnoreCase("Y")) {
			bookItem.put("PUBLISHER", publisher);
		}
		if (if_rev.equalsIgnoreCase("Y")){
			bookItem.put("REVIEWS", review);
		}

		hegde_book.insertOne(bookItem);

        System.out.println(title.toUpperCase() + " has been inserted.");

        input.close();

	}

}