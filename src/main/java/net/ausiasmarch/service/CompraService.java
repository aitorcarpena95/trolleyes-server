/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ausiasmarch.bean.CompraBean;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.CompraDao;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.setting.ConnectionSettings;

/**
 *
 * @author a033570312h
 */
public class CompraService implements ServiceInterface {

    HttpServletRequest oRequest = null;

    public CompraService(HttpServletRequest oRequest) {
        this.oRequest = oRequest;
    }

    @Override
    public String get() throws Exception {
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            int id = Integer.parseInt(oRequest.getParameter("id"));

            CompraDao oCompraDao = new CompraDao(oConnection);
            CompraBean oCompraBean = oCompraDao.get(id);

            Gson oGson = GsonFactory.getGson();
            String strJson = oGson.toJson(oCompraBean);
            return "{\"status\":200,\"message\":" + strJson + "}";
        } catch (NumberFormatException | SQLException ex) {
            String msg = this.getClass().getName() + " get method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
    }

    @Override
    public String getCount() throws Exception {
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            ResponseBean oResponseBean;
            Gson oGson = GsonFactory.getGson();
            CompraDao oCompraDao = new CompraDao(oConnection);
            Integer iCount = oCompraDao.getCount();
            if (iCount < 0) {
                oResponseBean = new ResponseBean(500, iCount.toString());
            } else {
                oResponseBean = new ResponseBean(200, iCount.toString());
            }
            return oGson.toJson(oResponseBean);
        } catch (SQLException ex) {
            String msg = this.getClass().getName() + " get method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
    }

    @Override
    public String getPage() throws Exception {
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            int iRpp = Integer.parseInt(oRequest.getParameter("rpp"));
            int iPage = Integer.parseInt(oRequest.getParameter("page"));
            List<String> orden = null;
            if (oRequest.getParameter("order") != null) {
                orden = Arrays.asList(oRequest.getParameter("order").split("\\s*,\\s*"));
            }
            CompraDao oCompraDao = new CompraDao(oConnection);
            ArrayList alCompraBean = oCompraDao.getPage(iPage, iRpp, orden);
            Gson oGson = GsonFactory.getGson();
            String strJson = oGson.toJson(alCompraBean);
            return "{\"status\":200,\"message\":" + strJson + "}";
        } catch (NumberFormatException | SQLException ex) {
            String msg = this.getClass().getName() + " get method ";
            throw new Exception(msg, ex);
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
    }

    @Override
    public String insert() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = null;
            Connection oConnection = null;
            try {
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                final GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                CompraBean oCompraBean = oGson.fromJson(oRequest.getParameter("data"), CompraBean.class);
                CompraDao oCompraDao = new CompraDao(oConnection);
                if (oCompraDao.insert(oCompraBean) == 0) {
                    oResponseBean = new ResponseBean(500, "KO");
                } else {
                    oResponseBean = new ResponseBean(200, "OK");
                };
                return oGson.toJson(oResponseBean);
            } catch (JsonSyntaxException | SQLException ex) {
                String msg = this.getClass().getName() + " get method ";
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oConnectionImplementation != null) {
                    oConnectionImplementation.disposeConnection();
                }
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
            return oGson.toJson(oResponseBean);
        }
    }

    @Override
    public String remove() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = null;
            Connection oConnection = null;
            try {
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                CompraDao oCompraDao = new CompraDao(oConnection);
                int id = Integer.parseInt(oRequest.getParameter("id"));
                if (oCompraDao.remove(id) == 0) {
                    oResponseBean = new ResponseBean(500, "KO");
                } else {
                    oResponseBean = new ResponseBean(200, "OK");
                }
                return oGson.toJson(oResponseBean);
            } catch (NumberFormatException | SQLException ex) {
                String msg = this.getClass().getName() + " get method ";
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oConnectionImplementation != null) {
                    oConnectionImplementation.disposeConnection();
                }
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
            return oGson.toJson(oResponseBean);
        }
    }

    @Override
    public String update() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = null;
            Connection oConnection = null;
            try {
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                CompraBean oCompraBean = new CompraBean();
                String data = oRequest.getParameter("data");
                oCompraBean = oGson.fromJson(data, CompraBean.class);
                CompraDao oCompraDao = new CompraDao(oConnection);
                if (oCompraDao.update(oCompraBean) == 0) {
                    oResponseBean = new ResponseBean(500, "KO");
                } else {
                    oResponseBean = new ResponseBean(200, "OK");
                }
                return oGson.toJson(oResponseBean);
            } catch (JsonSyntaxException | SQLException ex) {
                String msg = this.getClass().getName() + " get method ";
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oConnectionImplementation != null) {
                    oConnectionImplementation.disposeConnection();
                }
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
            return oGson.toJson(oResponseBean);
        }

    }

}
