package com.gkdesign.config;

import com.gkdesign.model.*;
import com.gkdesign.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Runs after DataSeeder (admin user). Each collection is seeded
 * independently and only if empty, so this is safe to leave in place
 * permanently — it never overwrites real data once any exists.
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class ContentSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final HomepageSectionRepository homepageSectionRepository;
    private final TestimonialRepository testimonialRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public void run(String... args) {
        seedCategories();
        seedProducts();
        seedHomepageSections();
        seedTestimonials();
        seedServices();
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        List<Category> categories = List.of(
                Category.builder().slug("furniture").name("Furniture").image("https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=800&q=80").description("Dining tables, lounge chairs, consoles and beds built from solid wood, finished by hand.").order(1).isHidden(false).build(),
                Category.builder().slug("lighting").name("Lighting").image("https://images.unsplash.com/photo-1524634126442-357e0eac3c14?w=800&q=80").description("Floor lamps, pendants and sconces designed to warm a room rather than flood it.").order(2).isHidden(false).build(),
                Category.builder().slug("wall-decor").name("Wall Decor").image("https://images.unsplash.com/photo-1615529328331-f8917597711f?w=800&q=80").description("Ceramic panels, framed textiles and sculptural pieces for walls that need a second look.").order(3).isHidden(false).build(),
                Category.builder().slug("mirrors").name("Mirrors").image("https://images.unsplash.com/photo-1618220179428-22790b461013?w=800&q=80").description("Brass, oak and iron-framed mirrors, beveled and finished to resist tarnish indoors.").order(4).isHidden(false).build(),
                Category.builder().slug("rugs").name("Rugs").image("https://images.unsplash.com/photo-1584346133934-a3044f11e0d5?w=800&q=80").description("Handwoven jute, wool and cotton rugs in natural fibers that soften with use.").order(5).isHidden(false).build(),
                Category.builder().slug("storage").name("Storage").image("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=800&q=80").description("Consoles, sideboards and cabinets built for real storage, not just display.").order(6).isHidden(false).build()
        );

        categoryRepository.saveAll(categories);
        log.info("Seeded {} categories.", categories.size());
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
                Product.builder()
                        .slug("aalborg-dining-table").sku("FUR-102").name("Aalborg Dining Table")
                        .category("Furniture").brand("Gk Design")
                        .price(58000).compareAtPrice(64000.0)
                        .material("Solid Teak Wood").finish("Natural Oiled")
                        .dimensions(Product.Dimensions.builder().l(180).w(90).h(76).unit("cm").build())
                        .weight("42 kg").stockStatus("Made to Order")
                        .colors(List.of(
                                Product.ProductColor.builder().name("Natural Teak").hex("#A9773F").build(),
                                Product.ProductColor.builder().name("Walnut Stain").hex("#5B3A29").build()))
                        .sizes(List.of("4-Seater (150cm)", "6-Seater (180cm)", "8-Seater (220cm)"))
                        .images(List.of(
                                "https://images.unsplash.com/photo-1592078615290-033ee584e267?w=1200&q=80",
                                "https://images.unsplash.com/photo-1615874959474-d609969a20ed?w=1200&q=80"))
                        .description("A six-seater dining table built from a single grade of solid teak, finished with a hand-rubbed natural oil that deepens in tone over the first year of use.")
                        .features(List.of("Solid teak, no veneer or laminate", "Mortise-and-tenon joinery", "Seats 6 comfortably, 8 at a squeeze"))
                        .careInstructions("Wipe with a dry or lightly damp cloth. Re-oil every 12–18 months.")
                        .deliveryInfo("Delivered assembled with white-glove placement in metro cities.")
                        .estimatedDeliveryDays("18–24 days (made to order)")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(true).isBestSeller(true)
                        .rating(4.8).ratingCount(32)
                        .build(),

                Product.builder()
                        .slug("meridian-lounge-chair").sku("FUR-118").name("Meridian Lounge Chair")
                        .category("Furniture").brand("Gk Design")
                        .price(24500)
                        .material("Walnut & Boucle Fabric").finish("Matte Lacquer")
                        .dimensions(Product.Dimensions.builder().l(78).w(82).h(74).unit("cm").build())
                        .weight("18 kg").stockStatus("In Stock")
                        .colors(List.of(
                                Product.ProductColor.builder().name("Oat Boucle").hex("#DCD3C2").build(),
                                Product.ProductColor.builder().name("Charcoal Boucle").hex("#3E3B37").build()))
                        .sizes(List.of("Standard"))
                        .images(List.of("https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=1200&q=80"))
                        .description("A low, wide-armed lounge chair built on a solid walnut frame with a boucle upholstered seat.")
                        .features(List.of("Solid walnut frame", "High-density foam seat", "Removable, dry-clean-only cover"))
                        .careInstructions("Vacuum fabric weekly. Spot clean only.")
                        .deliveryInfo("Delivered assembled across India.")
                        .estimatedDeliveryDays("7–10 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(true).isNewArrival(true)
                        .rating(4.9).ratingCount(51)
                        .build(),

                Product.builder()
                        .slug("arcata-brass-mirror").sku("MIR-041").name("Arcata Brass Mirror")
                        .category("Mirrors").brand("Gk Design")
                        .price(12800)
                        .material("Brushed Brass").finish("Antique Brass")
                        .dimensions(Product.Dimensions.builder().l(60).w(4).h(80).unit("cm").build())
                        .weight("6.5 kg").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Antique Brass").hex("#B08D57").build()))
                        .sizes(List.of("Small (45cm)", "Medium (60cm)", "Large (75cm)"))
                        .images(List.of("https://images.unsplash.com/photo-1618220179428-22790b461013?w=1200&q=80"))
                        .description("An arched wall mirror with a brushed-brass frame that resists tarnishing indoors.")
                        .features(List.of("Beveled 4mm glass", "Tarnish-resistant brass frame", "Concealed wall-mount hardware included"))
                        .careInstructions("Clean glass with standard glass cleaner.")
                        .deliveryInfo("Shipped in protective crate.")
                        .estimatedDeliveryDays("5–7 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false)
                        .rating(4.6).ratingCount(19)
                        .build(),

                Product.builder()
                        .slug("solstice-floor-lamp").sku("LGT-076").name("Solstice Floor Lamp")
                        .category("Lighting").brand("Gk Design")
                        .price(9400)
                        .material("Oak & Linen Shade").finish("Natural Oak")
                        .dimensions(Product.Dimensions.builder().l(40).w(40).h(150).unit("cm").build())
                        .weight("5 kg").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Natural Oak / Ivory Shade").hex("#C9A876").build()))
                        .sizes(List.of("Standard (150cm)"))
                        .images(List.of("https://images.unsplash.com/photo-1524634126442-357e0eac3c14?w=1200&q=80"))
                        .description("A tripod floor lamp with a solid oak frame and hand-stitched linen shade.")
                        .features(List.of("Solid oak tripod base", "Linen shade, replaceable", "In-line foot dimmer switch"))
                        .careInstructions("Dust shade with a soft brush.")
                        .deliveryInfo("Delivered flat-packed, 10-minute assembly.")
                        .estimatedDeliveryDays("4–6 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false).isBestSeller(true)
                        .rating(4.7).ratingCount(27)
                        .build(),

                Product.builder()
                        .slug("havana-storage-console").sku("STG-029").name("Havana Storage Console")
                        .category("Storage").brand("Gk Design")
                        .price(32500)
                        .material("Mango Wood & Cane").finish("Honey Wash")
                        .dimensions(Product.Dimensions.builder().l(140).w(40).h(80).unit("cm").build())
                        .weight("34 kg").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Honey Wash").hex("#C89B5C").build()))
                        .sizes(List.of("Standard (140cm)", "Compact (110cm)"))
                        .images(List.of("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=1200&q=80"))
                        .description("A three-drawer console in solid mango wood with hand-woven cane fronts.")
                        .features(List.of("3 soft-close drawers", "Hand-woven cane detailing", "Solid mango wood core"))
                        .careInstructions("Dust regularly. Avoid direct sunlight on cane panels.")
                        .deliveryInfo("Delivered flat-packed.")
                        .estimatedDeliveryDays("10–14 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false)
                        .rating(4.5).ratingCount(14)
                        .build(),

                Product.builder()
                        .slug("kiln-wall-panel-set").sku("WD-053").name("Kiln Ceramic Wall Panel Set")
                        .category("Wall Decor").brand("Gk Design")
                        .price(6200)
                        .material("Hand-glazed Ceramic").finish("Matte Glaze")
                        .dimensions(Product.Dimensions.builder().l(30).w(2).h(30).unit("cm").build())
                        .weight("3 kg (set of 4)").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Sandstone Matte").hex("#C7B79A").build()))
                        .sizes(List.of("Set of 4"))
                        .images(List.of("https://images.unsplash.com/photo-1615529328331-f8917597711f?w=1200&q=80"))
                        .description("A set of four hand-glazed ceramic panels, each fired individually.")
                        .features(List.of("Set of 4 panels, 30x30cm each", "Individually kiln-fired", "Includes 2 mounting layouts"))
                        .careInstructions("Dust with a dry cloth.")
                        .deliveryInfo("Shipped in individual foam crates.")
                        .estimatedDeliveryDays("6–8 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false).isNewArrival(true)
                        .rating(4.4).ratingCount(9)
                        .build(),

                Product.builder()
                        .slug("bergen-jute-rug").sku("RUG-014").name("Bergen Jute Rug")
                        .category("Rugs").brand("Gk Design")
                        .price(8900)
                        .material("Handwoven Jute").finish("Natural")
                        .dimensions(Product.Dimensions.builder().l(240).w(1).h(170).unit("cm").build())
                        .weight("7 kg").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Natural").hex("#D8C6A0").build()))
                        .sizes(List.of("5x7 ft", "6x9 ft", "8x10 ft"))
                        .images(List.of("https://images.unsplash.com/photo-1584346133934-a3044f11e0d5?w=1200&q=80"))
                        .description("A handwoven jute rug with a herringbone weave that adds texture underfoot.")
                        .features(List.of("100% natural jute", "Reversible weave", "Non-slip backing available"))
                        .careInstructions("Vacuum on low suction. Blot spills immediately.")
                        .deliveryInfo("Rolled and shipped in protective wrap.")
                        .estimatedDeliveryDays("5–7 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false)
                        .rating(4.6).ratingCount(22)
                        .build(),

                Product.builder()
                        .slug("porto-pendant-light").sku("LGT-091").name("Porto Pendant Light")
                        .category("Lighting").brand("Gk Design")
                        .price(5400)
                        .material("Rattan & Iron").finish("Natural Rattan")
                        .dimensions(Product.Dimensions.builder().l(35).w(35).h(30).unit("cm").build())
                        .weight("1.8 kg").stockStatus("In Stock")
                        .colors(List.of(Product.ProductColor.builder().name("Natural Rattan").hex("#C9A876").build()))
                        .sizes(List.of("Small (30cm)", "Large (40cm)"))
                        .images(List.of("https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=1200&q=80"))
                        .description("A hand-woven rattan pendant that casts a dappled, warm light pattern.")
                        .features(List.of("Hand-woven natural rattan", "Iron mounting frame", "E27 fitting, bulb not included"))
                        .careInstructions("Dust with a soft brush.")
                        .deliveryInfo("Shipped in reinforced box, ready to hang.")
                        .estimatedDeliveryDays("4–6 days")
                        .status(Product.ProductStatus.PUBLISHED)
                        .isLuxury(false).isBestSeller(true)
                        .rating(4.7).ratingCount(38)
                        .build()
        );

        productRepository.saveAll(products);
        log.info("Seeded {} products.", products.size());
    }

    private void seedHomepageSections() {
        if (homepageSectionRepository.count() > 0) return;

        List<HomepageSection> sections = List.of(
                HomepageSection.builder().id("hero").type("hero").label("Hero").order(1).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("category_grid").type("category_grid").label("Shop by Category").order(2).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("luxury_collection").type("featured_collection").label("Luxury Collection").order(3).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("affordable_collection").type("featured_collection").label("Everyday Collection").order(4).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("why_choose_us").type("why_choose_us").label("Why Choose Us").order(5).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("stats_band").type("stats_band").label("Statistics Band").order(6).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("how_it_works").type("how_it_works").label("How It Works").order(7).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("testimonials").type("testimonials").label("Testimonials").order(8).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("newsletter").type("newsletter").label("Newsletter").order(9).isEnabled(true).content(Map.of()).build(),
                HomepageSection.builder().id("contact_cta").type("contact_cta").label("Contact CTA").order(10).isEnabled(true).content(Map.of()).build()
        );

        homepageSectionRepository.saveAll(sections);
        log.info("Seeded {} homepage sections.", sections.size());
    }

    private void seedTestimonials() {
        if (testimonialRepository.count() > 0) return;

        List<Testimonial> testimonials = List.of(
                Testimonial.builder().customerName("Ritika Sen").location("Bengaluru")
                        .review("We furnished our entire living room through Gk Design over WhatsApp calls. Every dimension they gave us was accurate down to the centimetre.")
                        .rating(5).customerImage("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&q=80")
                        .isFeatured(true).isHidden(false).order(1).build(),
                Testimonial.builder().customerName("Arjun Malhotra").location("Delhi NCR")
                        .review("I was replacing one lamp and ended up redoing the study. Their team suggested finishes I hadn't considered.")
                        .rating(5).customerImage("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&q=80")
                        .isFeatured(true).isHidden(false).order(2).build(),
                Testimonial.builder().customerName("Priya Nair").location("Kochi")
                        .review("The Meridian chair is the first piece of furniture I've bought that made guests ask where it's from before they sit down.")
                        .rating(5).customerImage("https://images.unsplash.com/photo-1531123897727-8f129e1688ce?w=200&q=80")
                        .isFeatured(true).isHidden(false).order(3).build()
        );

        testimonialRepository.saveAll(testimonials);
        log.info("Seeded {} testimonials.", testimonials.size());
    }

    private void seedServices() {
        if (serviceOfferingRepository.count() > 0) return;

        List<ServiceOffering> services = List.of(
                ServiceOffering.builder().slug("custom-design").title("Custom Design").subtitle("One-off pieces, built to your dimensions")
                        .description("Send us the space, the constraints, and the feeling you're after. We'll sketch options, confirm materials and dimensions with you before anything is cut.")
                        .icon("compass").gallery(List.of("https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=1000&q=80"))
                        .features(List.of("Free initial consultation", "2-3 design revisions included", "Material and finish samples sent before production"))
                        .order(1).isHidden(false).build(),
                ServiceOffering.builder().slug("bulk-trade-orders").title("Bulk & Trade Orders").subtitle("For designers, architects and hospitality projects")
                        .description("Furnishing multiple units or a commercial space? We work directly with designers and procurement teams on volume pricing and phased delivery.")
                        .icon("package").gallery(List.of("https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80"))
                        .features(List.of("Trade pricing on orders of 10+ units", "Dedicated account contact", "Phased delivery scheduling"))
                        .order(2).isHidden(false).build(),
                ServiceOffering.builder().slug("interior-consultation").title("Interior Consultation").subtitle("An hour with our design team, in person or on call")
                        .description("Not sure what a room needs? A consultation gets you a second opinion on layout, material pairing and what to prioritize first.")
                        .icon("ruler").gallery(List.of("https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?w=1000&q=80"))
                        .features(List.of("60-minute session, in-showroom or video call", "Follow-up mood board within 3 days", "Credited toward any order within 30 days"))
                        .order(3).isHidden(false).build(),
                ServiceOffering.builder().slug("restoration-repair").title("Restoration & Repair").subtitle("For Gk Design pieces, and most solid-wood furniture")
                        .description("Re-oiling a dining table, replacing upholstery, fixing a joint that's loosened after a decade of use — our workshop handles repair work too.")
                        .icon("wrench").gallery(List.of("https://images.unsplash.com/photo-1581783898377-1c85bf937427?w=1000&q=80"))
                        .features(List.of("On-site assessment for large pieces", "Original material matching where possible", "Typical turnaround: 2-3 weeks"))
                        .order(4).isHidden(false).build()
        );

        serviceOfferingRepository.saveAll(services);
        log.info("Seeded {} services.", services.size());
    }
}
