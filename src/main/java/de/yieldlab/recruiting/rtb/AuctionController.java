package de.yieldlab.recruiting.rtb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.yieldlab.recruiting.rtb.domain.Auction;
import de.yieldlab.recruiting.rtb.domain.BidResponseXO;
import de.yieldlab.recruiting.rtb.templating.TemplatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class AuctionController {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionController.class);

    // hardcode because for some reason even @Configuration won't load an array
    private static final List<URI> uris = Lists.newArrayList(
            URI.create("http://localhost:8081"),
            URI.create("http://localhost:8082"),
            URI.create("http://localhost:8083")
    );

    @Autowired
    public AuctionController(TemplatingService templating ) {
        this.templating = templating;
    }

    private TemplatingService templating;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String auction(@PathVariable String id, @RequestParam Map<String, String> attributes) {
        Auction auction = new Auction(id, attributes, uris);

        LOG.debug("bids: {}", auction.bids());

        BidResponseXO winningResponse = auction.winner().getResponse();
        if(winningResponse != null) {
            String adContent = winningResponse.getContent();
            return templating.process(adContent, ImmutableMap.of("price", winningResponse.getBid()));
        } else {
            return "No valid bids";
        }
    }
}

