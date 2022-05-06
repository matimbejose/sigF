package br.com.villefortconsulting.sgfinancas.integratorclient.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOriginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ip;

    private String userAgent;

    private String timeOnScreen;

    private String device;

    private String browser;

    private String platform;

    private String engine;

    private transient Object application;

    private boolean isPartner;

    private MarketingMediaDTO marketingMedia;

}
