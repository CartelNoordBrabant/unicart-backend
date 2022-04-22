package io.cartel.noord.brabant.api.controllers;

import io.cartel.noord.brabant.api.dtos.DiffResponse;
import io.cartel.noord.brabant.domain.services.DiffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Tag(name = "Diff", description = "every diff endpoint")
@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private final DiffService service;

    public DiffController(DiffService service) {
        this.service = service;
    }

    @Operation(
            summary = "Save left side",
            description = """
                    For a given **ID** it will save (for a time) a **base64 encoded JSON** that can be
                    [compared](#/Diff/getDiff) to the [right side](#/Diff/saveRight).
                    """)
    @ResponseStatus(ACCEPTED)
    @PostMapping("/{id}/left")
    public void saveLeft(@PathVariable UUID id, @RequestBody String data) {
        service.saveLeft(id, data);
    }

    @Operation(
            summary = "Save right side",
            description = """
                    For a given **ID** it will save (for a time) a **base64 encoded JSON** that can be
                    [compared](#/Diff/getDiff) to the [left side](#/Diff/saveLeft).
                    """)
    @ResponseStatus(ACCEPTED)
    @PostMapping(value = "/{id}/right")
    public void saveRight(@PathVariable UUID id, @RequestBody String data) {
        service.saveRight(id, data);
    }

    @Operation(
            summary = "Get diff from left and right",
            description = """
                    For a given **ID** it compares JSON data saved to [left](#/Diff/saveLeft)
                    and [right](#/Diff/saveRight) sides, and returns a side-by-side diff.
                    """)
    @GetMapping("/{id}")
    public DiffResponse getDiff(@PathVariable UUID id) {
        return service.getById(id);
    }
}
