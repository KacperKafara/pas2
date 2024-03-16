package p.lodz.pl.pas2.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Base64;
import java.util.UUID;

@Component
public class Jws {

    @Value("${security.jws.token.secret-key:secret-value}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateSign(UUID id) throws JOSEException {
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(id.toString()));
        JWSSigner signer = new MACSigner(secretKey);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    private boolean verify(String token) throws JOSEException, ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey);
        return jwsObject.verify(verifier);
    }

    public boolean verifySign(String token, UUID id) {
        try {
            if(!verify(token)) return false;
            String newSign = generateSign(id);
            return token.equals(newSign);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

}
