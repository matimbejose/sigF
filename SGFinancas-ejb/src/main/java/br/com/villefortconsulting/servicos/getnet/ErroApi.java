/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroApi {

    private Long timestamp;

    private String message;

    private String details;

    private String status;

}
