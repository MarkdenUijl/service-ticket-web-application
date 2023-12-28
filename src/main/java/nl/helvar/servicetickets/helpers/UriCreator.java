package nl.helvar.servicetickets.helpers;

import nl.helvar.servicetickets.interfaces.Identifyable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriCreator {
    public static URI createUri(Identifyable uriObject) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getId())
                        .toUriString()
        );

        return uri;
    }
}
