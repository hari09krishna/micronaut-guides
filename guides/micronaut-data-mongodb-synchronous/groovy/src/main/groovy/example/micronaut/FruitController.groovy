package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.Status;

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Controller("/fruits") // <1>
class FruitController {

    private final FruitService fruitService

    FruitController(FruitService fruitService) {  // <2>
        this.fruitService = fruitService
    }

    @Get  // <3>
    Iterable<Fruit> list() {
        fruitService.list()
    }

    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    Fruit save(@NonNull @NotNull @Valid Fruit fruit) { // <6>
        fruitService.save(fruit)
    }

    @Put
    Fruit update(@NonNull @NotNull @Valid Fruit fruit) {
        fruitService.save(fruit)
    }

    @Get("/{id}") // <7>
    Optional<Fruit> find(@PathVariable String id) {
        fruitService.find(id)
    }

    @Get("/q") // <8>
    Iterable<Fruit> query(@QueryValue @NotNull List<String> names) { // <9>
        fruitService.findByNameInList(names)
    }
}
