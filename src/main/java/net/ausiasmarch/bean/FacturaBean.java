
package net.ausiasmarch.bean;

import com.google.gson.annotations.Expose;
import java.util.Date;


public class FacturaBean implements BeanInterface {

    @Expose
    private Integer id;

    @Expose
    private Integer iva;

    @Expose
    private Date fecha;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public Date getFecha() {
        return fecha;
    }


    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    

}
