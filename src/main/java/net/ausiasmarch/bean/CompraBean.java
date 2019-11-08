/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.bean;

import com.google.gson.annotations.Expose;

/**
 *
 * @author a033570312h
 */
public class CompraBean implements BeanInterface {
    
    @Expose
    private Integer id;

    @Expose
    private int cantidad;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }  

}
