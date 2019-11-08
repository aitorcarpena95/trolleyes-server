package net.ausiasmarch.factory;

import javax.servlet.http.HttpServletRequest;
import net.ausiasmarch.service.CarritoService;
import net.ausiasmarch.service.ProductoService;
import net.ausiasmarch.service.UsuarioService;

public class ServiceCall {

    HttpServletRequest oRequest;

    public static String executeService(HttpServletRequest oRequest) throws Exception {
        String ob = oRequest.getParameter("ob");
        String op = oRequest.getParameter("op");
        String strResult = null;
        if (ob.equalsIgnoreCase("usuario")) {
            UsuarioService oUsuarioService = new UsuarioService(oRequest);
            switch (op) {
                case "login":
                    strResult = oUsuarioService.login();
                    break;
                case "check":
                    strResult = oUsuarioService.check();
                    break;
                case "logout":
                    strResult = oUsuarioService.logout();
                    break;
            }
        } else if (ob.equalsIgnoreCase("producto")) {
            ProductoService oProductoService = new ProductoService(oRequest);
            switch (op) {
                case "get":
                    strResult = oProductoService.get();
                    break;
                case "update":
                    strResult = oProductoService.update();
                    break;
                case "remove":
                    strResult = oProductoService.remove();
                    break;
                case "insert":
                    strResult = oProductoService.insert();
                    break;
                case "getpage":
                    strResult = oProductoService.getPage();
                    break;
                case "getcount":
                    strResult = oProductoService.getCount();
                    break;
            }

        } else if (ob.equalsIgnoreCase("carrito")) {
            CarritoService oCarritoService = new CarritoService(oRequest);
            switch (op) {
                case "add":
                    strResult = oCarritoService.add();
                    break;
                case "list":
                    strResult = oCarritoService.list();
                    break;
                case "remove":
                    strResult = oCarritoService.remove();
                    break;
                case "empty":
                    strResult = oCarritoService.empty();
                    break;
            }
        }
        return strResult;
    }
}