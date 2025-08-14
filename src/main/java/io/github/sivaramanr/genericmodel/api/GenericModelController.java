package io.github.sivaramanr.genericmodel.api;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.genericmodel.facade.GenericModelFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericModelBean> get(@RequestHeader("tenantId") String tenantId,
                                @PathVariable("id") String id) {
        return genericModelFacade
            .getGenericModel(tenantId, id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @RequestMapping("/genericmodel/api-docs")
    public String swaggerConfig() {
        return "forward:/v3/api-docs";
    }

}
