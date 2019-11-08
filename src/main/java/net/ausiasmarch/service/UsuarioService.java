package net.ausiasmarch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.bean.UsuarioBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.UsuarioDao;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.setting.ConnectionSettings;

public class UsuarioService {

    HttpServletRequest oRequest = null;

    public UsuarioService(HttpServletRequest oRequest) {
        this.oRequest = oRequest;
    }

    public String login() {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean = null;
        if (oRequest.getParameter("username").equals("rafa") && oRequest.getParameter("password").equalsIgnoreCase("017FBC0E001B5E9C16908C754F9607DC886F25D08B2CBADC788B8B267DF199F2")) {
            oSession.setAttribute("usuario", oRequest.getParameter("username"));
            oResponseBean = new ResponseBean(200, "Welcome");
        } else {
            oResponseBean = new ResponseBean(500, "Wrong password");
        }
        Gson oGson = GsonFactory.getGson();
        return oGson.toJson(oResponseBean);
    }

    public String check() {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean = null;
        if (oSession.getAttribute("usuario") != null) {
            oResponseBean = new ResponseBean(200, (String) oSession.getAttribute("usuario"));
        } else {
            oResponseBean = new ResponseBean(500, "No active session");
        }
        Gson oGson = GsonFactory.getGson();
        return oGson.toJson(oResponseBean);
    }

    public String logout() {
        HttpSession oSession = oRequest.getSession();
        oSession.invalidate();
        ResponseBean oResponseBean = null;
        oResponseBean = new ResponseBean(200, "No active session");
        Gson oGson = GsonFactory.getGson();
        return oGson.toJson(oResponseBean);
    }

    public String get() throws Exception {
        ConnectionInterface oConnectionImplementation = null;
        Connection oConnection = null;
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            int id = Integer.parseInt(oRequest.getParameter("id"));
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            UsuarioBean oUsuarioBean = oUsuarioDao.get(id);
            Gson oGson = GsonFactory.getGson();
            String strJson = oGson.toJson(oUsuarioBean);
            return "{\"status\":200,\"message\":" + strJson + "}";
        } catch (Exception ex) {
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

    public String getPage() throws SQLException {
        ConnectionInterface oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
        Connection oConnection = oConnectionImplementation.newConnection();
        int iRpp = Integer.parseInt(oRequest.getParameter("rpp"));
        int iPage = Integer.parseInt(oRequest.getParameter("page"));
        List<String> orden = null;
        if (oRequest.getParameter("order") != null) {
            orden = Arrays.asList(oRequest.getParameter("order").split("\\s*,\\s*"));
        }
        UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
        ArrayList alPostBean = oUsuarioDao.getPage(iPage, iRpp, orden);
        Gson oGson = GsonFactory.getGson();
        String strJson = oGson.toJson(alPostBean);
        if (oConnection != null) {
            oConnection.close();
        }
        if (oConnectionImplementation != null) {
            oConnectionImplementation.disposeConnection();
        }
        return "{\"status\":200,\"message\":" + strJson + "}";
    }

    public String getCount() throws SQLException {
        ConnectionInterface oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
        Connection oConnection = oConnectionImplementation.newConnection();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
        Integer iCount = oUsuarioDao.getCount();
        if (oConnection != null) {
            oConnection.close();
        }
        if (oConnectionImplementation != null) {
            oConnectionImplementation.disposeConnection();
        }
        if (iCount < 0) {
            oResponseBean = new ResponseBean(500, iCount.toString());
        } else {
            oResponseBean = new ResponseBean(200, iCount.toString());
        }
        return oGson.toJson(oResponseBean);
    }

    public String update() throws SQLException {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            Connection oConnection = oConnectionImplementation.newConnection();
            UsuarioBean oUsuarioBean = new UsuarioBean();
            String data = oRequest.getParameter("data");
            oUsuarioBean = oGson.fromJson(data, UsuarioBean.class);
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            if (oUsuarioDao.update(oUsuarioBean) == 0) {
                oResponseBean = new ResponseBean(500, "KO");
            } else {
                oResponseBean = new ResponseBean(200, "OK");
            }
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
        }
        return oGson.toJson(oResponseBean);
    }

    public String insert() throws SQLException {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            Connection oConnection = oConnectionImplementation.newConnection();
            final GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            UsuarioBean oUsuarioBean = oGson.fromJson(oRequest.getParameter("data"), UsuarioBean.class);
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            if (oUsuarioDao.insert(oUsuarioBean) == 0) {
                oResponseBean = new ResponseBean(500, "KO");
            } else {
                oResponseBean = new ResponseBean(200, "OK");
            };
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
        }
        return oGson.toJson(oResponseBean);
    }

    public String remove() throws SQLException {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            Connection oConnection = oConnectionImplementation.newConnection();
            UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
            int id = Integer.parseInt(oRequest.getParameter("id"));
            if (oUsuarioDao.remove(id) == 0) {
                oResponseBean = new ResponseBean(500, "KO");
            } else {
                oResponseBean = new ResponseBean(200, "OK");
            }
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        } else {
            oResponseBean = new ResponseBean(401, "Error: No session");
        }
        return oGson.toJson(oResponseBean);
    }

    public String fill() throws Exception {
        HttpSession oSession = oRequest.getSession();
        ResponseBean oResponseBean;
        Gson oGson = GsonFactory.getGson();
        if (oSession.getAttribute("usuario") != null) {
            ConnectionInterface oConnectionImplementation = null;
            Connection oConnection = null;
            try {
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
                int numPost = Integer.parseInt(oRequest.getParameter("number"));
                for (int i = 0; i < numPost; i++) {
                    UsuarioBean oUsuarioBean = new UsuarioBean();

                    oUsuarioBean.setNombre(generaNombre(1));
                    /*  oUsuarioBean.setCuerpo(generaTexto(this.getRandomInt(5, 25)));
                    oUsuarioBean.setEtiquetas(generaPalabras(this.getRandomInt(1, 6)));
                    oUsuarioBean.setFecha(randomDate);*/
                    oUsuarioDao.insert(oUsuarioBean);
                }
                oResponseBean = new ResponseBean(200, "Insertados los registros con exito");
                return oGson.toJson(oResponseBean);
            } catch (Exception ex) {
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

    String[] nombreUsuario = {"Aitor", "Ruben", "Daniel", "Lucia", "David", "Marta", "Alejandro", "Anna", "Mercedes", "Julia", "Hector", "Claudia"};
    String[] apellidoUsuario = {"Frances", "Herrera", "Martínez", "Belda", "Ferre", "Silvestre", "Garcia", "Calvo", "Gutierrez", "Rodriguez", "Gomez", "Jimenez", "Sanchez", "Alonso", "Torres", "Sanz", "Cruz", "Leon", "Arias"};

    private int getRandomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private String generaNombre(int longitud) {
        String nombreRandom = "";
        for (int i = 0; i < longitud; i++) {
            nombreRandom += nombreUsuario[(int) (Math.random() * nombreUsuario.length) + 0];
        }
        return nombreRandom;
    }
    /* 
    private String generaTexto(int longitud) {
        String fraseRandom = "";
        for (int i = 0; i < longitud; i++) {
            fraseRandom += frasesInicio[(int) (Math.random() * frasesInicio.length) + 0];
            fraseRandom += frasesMitad[(int) (Math.random() * frasesMitad.length) + 0];
            fraseRandom += frasesFinal[(int) (Math.random() * frasesFinal.length) + 0];
        }
        return fraseRandom;
    }

    private String generaPalabras(int longitud) {
        Collections.shuffle(Arrays.asList(palabras));
        String[] listaPalabras = Arrays.copyOfRange(palabras, 0, longitud);        
        String palabrasRandom = "";
        for (int i = 0; i < longitud; i++) {
            palabrasRandom += palabras[i];
            palabrasRandom += " ";
        }
        return palabrasRandom;
    }
     */
}
