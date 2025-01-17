/*
 * Library
 * This is a library API
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package example.micronaut.controller;

//tag::import[]
import example.micronaut.BookEntity;
import example.micronaut.BookRepository;
import example.micronaut.BookSpecifications;
import example.micronaut.model.BookAvailability;
import example.micronaut.model.BookInfo;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;
import static io.micronaut.http.HttpStatus.OK;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;
import io.micronaut.http.annotation.Status;
//end::import[]

@Generated(value="org.openapitools.codegen.languages.JavaMicronautServerCodegen", date="2022-01-31T13:21:29.860-05:00[America/Toronto]")
@Controller("${context-path}")
public class BooksController {

    //tag::inject[]
    private final BookRepository bookRepository; // <1>

    public BooksController(BookRepository bookRepository) { // <1>
        this.bookRepository = bookRepository;
    }
    //end::inject[]

    /**
     * Add a new book
     *
     * @param bookInfo  (required)
     */
    @ApiOperation(
            value = "Add a new book",
            nickname = "addBook",
            authorizations = {},
            tags={})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @Post(uri="/add")
    @Produces(value = {})
    @Consumes(value = {"application/json"})
    @Secured(SecurityRule.IS_ANONYMOUS)
    //tag::addBook[]
    @ExecuteOn(TaskExecutors.IO) // <1>
    @Status(OK) // <2>
    public void addBook(@Body @NotNull @Valid BookInfo bookInfo) {
        bookRepository.save(bookInfo.getName(), // <3>
                bookInfo.getAvailability(),
                bookInfo.getAuthor(),
                bookInfo.getISBN());
    }
    //end::addBook[]

    /**
     * Search for a book
     *
     * @param bookName  (optional)
     * @param authorName  (optional)
     * @return List&lt;BookInfo&gt;
     */
    @ApiOperation(
            value = "Search for a book",
            nickname = "search",
            response = BookInfo.class,
            responseContainer = "array",
            authorizations = {},
            tags={})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BookInfo.class, responseContainer = "array"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @Get(uri="/search")
    @Produces(value = {"applicaton/json"})
    @Secured(SecurityRule.IS_ANONYMOUS)
    //tag::search[]
    @ExecuteOn(TaskExecutors.IO) // <1>
    public List<BookInfo> search(
            @QueryValue(value="book-name") @Nullable @Size(min=3) String bookName,
            @QueryValue(value="author-name") @Nullable String authorName) {
        return searchEntities(bookName, authorName)
                .stream()
                .map(this::map) // <5>
                .collect(Collectors.toList());
    }

    private BookInfo map(BookEntity entity) {
        BookInfo book = new BookInfo(entity.getName(), entity.getAvailability());
        book.setISBN(entity.getIsbn());
        book.setAuthor(entity.getAuthor());
        return book;
    }

    @NonNull
    private List<BookEntity> searchEntities(@Nullable String name, @Nullable String author) { // <2>
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(author)) {
            return bookRepository.findAll();
        } else if (StringUtils.isEmpty(name)) {
            return bookRepository.findAll(BookSpecifications.authorLike(author)); // <3>

        } else  if (StringUtils.isEmpty(author)) {
            return bookRepository.findAll(BookSpecifications.nameLike(name));
        } else {
            return bookRepository.findAll(BookSpecifications.authorLike(author)
                    .and(BookSpecifications.nameLike(name))); // <4>
        }
    }
    //end::search[]
}
