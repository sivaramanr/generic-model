package io.github.sivaramanr.genericmodel.api;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.api.validator.MaxPageSize;
import io.github.sivaramanr.genericmodel.facade.GenericModelTranslationFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/genericmodel")
public class GenericModelTranslatorController {

    @Autowired
    private GenericModelTranslationFacade genericModelTranslationFacade;

    @PostMapping(value = "{id}/{field}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestHeader("tenantId") String tenantId,
                                       @RequestHeader("username") String username,
                                       @PathVariable("id") String id,
                                       @PathVariable("field") String field,
                                       @Valid @RequestBody GenericModelTranslationBean bean) {
        final String translationId = genericModelTranslationFacade.createGenericModelTranslation(tenantId, id, field,
            bean, username);
        final StringBuilder uriBuilder = new StringBuilder("/genericmodel/")
            .append(id)
            .append("/")
            .append(field)
            .append("/")
            .append(translationId);
        return ResponseEntity.created(URI.create(uriBuilder.toString())).build();
    }

    @PutMapping(value = "{id}/{field}/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestHeader("tenantId") String tenantId,
                                       @RequestHeader("username") String username,
                                       @PathVariable("id") String id,
                                       @PathVariable("field") String field,
                                       @PathVariable("locale") String locale,
                                       @RequestBody Map<String, String> payload) {
        if (!payload.containsKey("text")) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        genericModelTranslationFacade.updateGenericModelTranslation(tenantId, id, field, locale, payload.get("text"), username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "{id}/{field}/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericModelTranslationBean> get(@RequestHeader("tenantId") String tenantId,
                                                           @PathVariable("id") String id,
                                                           @PathVariable("field") String field,
                                                           @PathVariable("locale") String locale) {
        return genericModelTranslationFacade.getGenericModelTranslation(tenantId, id, field, locale)

            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "{id}/{field}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GenericModelTranslationBean> search(@RequestHeader("tenantId") String tenantId,
        @PathVariable("id") String id,
        @PathVariable("field") String field,
        @RequestParam(value = "page", defaultValue = "${generic-model.api.default-page}") Integer page,
        @MaxPageSize @Min(value = 0, message = "Page size must be negative")
        @RequestParam(value = "size", defaultValue = "${generic-model.api.default-page-size}") Integer size) {
        return genericModelTranslationFacade.getGenericModelTranslations(tenantId, id, field, page, size);
    }

    @DeleteMapping(value = "{id}/{field}/{locale}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestHeader("tenantId") String tenantId,
                                                           @PathVariable("id") String id,
                                                           @PathVariable("field") String field,
                                                           @PathVariable("locale") String locale) {
        genericModelTranslationFacade.deleteGenericModelTranslation(tenantId, id, field, locale);
        return ResponseEntity.noContent().build();
    }

}
