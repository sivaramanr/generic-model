package io.github.sivaramanr.genericmodel.api;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.genericmodel.api.validator.MaxPageSize;
import io.github.sivaramanr.genericmodel.facade.GenericModelFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/genericmodel")
@Tag(name = "${openapi.swagger.apiInfo.genericModel.name}")
public class GenericModelController {

    @Autowired
    private GenericModelFacade genericModelFacade;

    @Operation(operationId="create", summary = "${openapi.swagger.apiInfo.genericModel.create.summary}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {})
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestHeader("tenantId") String tenantId,
                                       @RequestHeader("username") String username,
                                       @Valid @RequestBody GenericModelBean bean) {
        final String genericModelId = genericModelFacade.createGenericModel(tenantId, bean, username);
        return ResponseEntity.created(URI.create("/genericmodel/" + genericModelId)).build();
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestHeader("tenantId") String tenantId,
                                       @RequestHeader("username") String username,
                                       @PathVariable("id") String id,
                                       @Valid @RequestBody GenericModelBean bean) {
        genericModelFacade.updateGenericModel(tenantId, id, bean, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericModelBean> get(@RequestHeader("tenantId") String tenantId,
                                                @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                @PathVariable("id") String id) {
        return genericModelFacade
            .getGenericModel(tenantId, id, acceptLanguage)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GenericModelBean> search(@RequestHeader("tenantId") String tenantId,
                                         @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                         @RequestParam("genericType") GenericType genericType,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "statusType", required = false) StatusType statusType,
                                         @RequestParam(value = "page", defaultValue = "${generic-model.api.default-page}") Integer page,
                                         @MaxPageSize @Min(value = 0, message = "Page size must be negative")
                                         @RequestParam(value = "size", defaultValue = "${generic-model.api.default-page-size}") Integer size) {
        return genericModelFacade.search(tenantId, genericType, name, statusType, acceptLanguage, page, size);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestHeader("tenantId") String tenantId,
                                                   @RequestHeader("username") String username,
                                                   @PathVariable("id") String id) {
        genericModelFacade.deleteGenericModel(tenantId, id, username);
        return ResponseEntity.noContent().build();
    }

}
