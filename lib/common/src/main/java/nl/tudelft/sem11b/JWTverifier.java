package nl.tudelft.sem11b;

public class JWTverifier {

    //TODO: make this better
    private static String secret = "super-secret";

    public static DecodedJWT verify(String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded;
    }
}
