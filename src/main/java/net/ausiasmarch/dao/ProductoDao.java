/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import net.ausiasmarch.bean.BeanInterface;
import net.ausiasmarch.bean.ProductoBean;

/**
 *
 * @author a033570312h
 */
public class ProductoDao implements DaoInterface {
    
    Connection oConnection = null;

    public ProductoDao(Connection oConnection) {
        this.oConnection = oConnection;
    }

    @Override
    public ProductoBean get(int id) throws SQLException {
        PreparedStatement oPreparedStatement;
        ResultSet oResultSet;
        String strSQL = "SELECT * FROM producto WHERE id=?";
        oPreparedStatement = oConnection.prepareStatement(strSQL);
        oPreparedStatement.setInt(1, id);
        oResultSet = oPreparedStatement.executeQuery();
        ProductoBean oProductoBean;
        if (oResultSet.next()) {
            oProductoBean = new ProductoBean();
            oProductoBean.setId(oResultSet.getInt("id"));
            oProductoBean.setCodigo(oResultSet.getString("codigo"));
            oProductoBean.setDescripcion(oResultSet.getString("descripcion"));
            oProductoBean.setExistencias(oResultSet.getInt("existencias"));
            oProductoBean.setPrecio(oResultSet.getDouble("precio"));
            oProductoBean.setImagen(oResultSet.getString("imagen"));
        } else {
            oProductoBean = null;
        }
        return oProductoBean;
    }

    @Override
    public int getCount() throws SQLException {
        PreparedStatement oPreparedStatement;
        ResultSet oResultSet;
        String strSQL = "SELECT count(*) FROM producto";
        oPreparedStatement = oConnection.prepareStatement(strSQL);
        oResultSet = oPreparedStatement.executeQuery();
        if (oResultSet.next()) {
            return oResultSet.getInt(1);
        } else {
            return -1;
        }
    }

    @Override
    public ArrayList<ProductoBean> getPage(int page, int limit, List<String> orden) throws SQLException {

        PreparedStatement oPreparedStatement;
        ResultSet oResultSet;
        int offset;

        if (page == 1) {
            offset = 0;
        } else {
            offset = (limit * page) - limit;
        }

        if (orden == null) {
            oPreparedStatement = oConnection.prepareStatement("SELECT * FROM producto LIMIT ? OFFSET ?");
            oPreparedStatement.setInt(1, limit);
            oPreparedStatement.setInt(2, offset);
        } else {
        	String sqlQuery = "SELECT * FROM producto ";
        	sqlQuery += "ORDER BY ";
        	for (int i = 1; i <= orden.size(); i++) {
        		if (orden.get((i-1)).equalsIgnoreCase("asc")) {
        			sqlQuery += "ASC ";
        		} else if (orden.get((i-1)).equalsIgnoreCase("desc")) {
        			sqlQuery += "DESC ";
        		} else {
        			sqlQuery += "? ";
        		}
        	}
        	sqlQuery += "LIMIT ? OFFSET ?";
        	oPreparedStatement = oConnection.prepareStatement(sqlQuery);
        	for (int i = 1; i < orden.size(); i++) {
        		if (orden.get((i-1)).equalsIgnoreCase("id")) {
        			oPreparedStatement.setInt(i, 1);
        		} else if (orden.get((i-1)).equalsIgnoreCase("codigo")) {
        			oPreparedStatement.setInt(i, 2);
        		} else if (orden.get((i-1)).equalsIgnoreCase("existencias")) {
        			oPreparedStatement.setInt(i, 3);
        		} else if (orden.get((i-1)).equalsIgnoreCase("precio")) {
        			oPreparedStatement.setInt(i, 4);
        		} else if (orden.get((i-1)).equalsIgnoreCase("descripcion")) {
        			oPreparedStatement.setInt(i, 5);
        		}
        		
        	}
        	oPreparedStatement.setInt((orden.size()), limit);
            oPreparedStatement.setInt((orden.size()+1), offset);
        }
        
        oResultSet = oPreparedStatement.executeQuery();

        ArrayList<ProductoBean> oProductoBeanList = new ArrayList<>();
        while (oResultSet.next()) {
            ProductoBean oProductoBean = new ProductoBean();
            oProductoBean.setId(oResultSet.getInt("id"));
            oProductoBean.setCodigo(oResultSet.getString("codigo"));
            oProductoBean.setDescripcion(oResultSet.getString("descripcion"));
            oProductoBean.setExistencias(oResultSet.getInt("existencias"));
            oProductoBean.setPrecio(oResultSet.getDouble("precio"));
            oProductoBean.setImagen(oResultSet.getString("imagen"));

            oProductoBeanList.add(oProductoBean);
        }

        return oProductoBeanList;
    }

    @Override
    public Integer insert(BeanInterface oProductoBeanParam) throws SQLException {
        PreparedStatement oPreparedStatement;
        String strsql = "INSERT INTO producto (codigo,existencias,precio,imagen,descripcion) VALUES(?,?,?,?,?)";
        oPreparedStatement = oConnection.prepareStatement(strsql);
        ProductoBean oProductoBean = (ProductoBean) oProductoBeanParam;
        oPreparedStatement.setString(1, oProductoBean.getCodigo());
        oPreparedStatement.setInt(2, oProductoBean.getExistencias());
        oPreparedStatement.setDouble(3, oProductoBean.getPrecio());
        oPreparedStatement.setString(4, oProductoBean.getImagen());
        oPreparedStatement.setString(5, oProductoBean.getDescripcion());
        int iResult = oPreparedStatement.executeUpdate();
        return iResult;    
    }

    @Override
    public Integer remove(int id) throws SQLException {
        PreparedStatement oPreparedStament = null;
        oPreparedStament = oConnection.prepareStatement("DELETE FROM producto WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
        oPreparedStament.setInt(1, id);
        int iResult = oPreparedStament.executeUpdate();
        return iResult;
    }

    @Override
    public Integer update(BeanInterface oProductoBeanParam) throws SQLException {
        PreparedStatement oPreparedStatement = null;
        String strSQL = "UPDATE producto SET codigo = ?, existencias = ?, precio = ?, imagen = ?, descripcion = ? WHERE id = ?";
        int iResult;
        oPreparedStatement = oConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
        ProductoBean oProductoBean = (ProductoBean) oProductoBeanParam;
        oPreparedStatement.setString(1, oProductoBean.getCodigo());
        oPreparedStatement.setInt(2, oProductoBean.getExistencias());
        oPreparedStatement.setDouble(3, oProductoBean.getPrecio());
        oPreparedStatement.setString(4, oProductoBean.getImagen());
        oPreparedStatement.setString(5, oProductoBean.getDescripcion());
        oPreparedStatement.setInt(6, oProductoBean.getId());
        iResult = oPreparedStatement.executeUpdate();
        return iResult;
    }
}
