package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@Service
public class Clients extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;

    @PostMapping("/register-clients")
    public ResponseEntity<Map<String, Object>> setClientInfos(@RequestBody Client client) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        if (conn != null) {
            try {
                String checkClient = "SELECT COUNT(*) FROM clients WHERE client_CPF=?";
                pstmt = conn.prepareStatement(checkClient);
                pstmt.setString(1, client.getClient_CPF());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    response.put("error", "Cliente (CPF) já cadastrado!");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }

                if (client.getClient_CEP().length() == 8) {
                    if (client.getClient_CPF().length() == 11) {
                        String sql = "INSERT INTO clients (client_id, client_name, client_address, client_CPF, client_type, client_CEP, client_activity) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        String id = String.valueOf(client.getClientID());
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, id);
                        pstmt.setString(2, client.getClient_name());
                        pstmt.setString(3, client.getClient_address());
                        pstmt.setString(4, client.getClient_CPF());
                        pstmt.setString(5, client.isClient_type());
                        pstmt.setString(6, client.getClient_CEP());
                        pstmt.setBoolean(7, true);
                        pstmt.executeUpdate();

                        response.put("client_id", client.getClientID());
                        response.put("client_name", client.getClient_name());
                        response.put("client_address", client.getClient_address());
                        response.put("client_CPF", client.getClient_CPF());
                        response.put("client_CEP", client.getClient_CEP());
                        response.put("client_activity", true);
                        response.put("client_type", client.isClient_type());

                        return ResponseEntity.ok(response);

                    } else {
                        response.put("error", "CPF inválido.");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                } else {
                    response.put("error", "CEP inválido.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (SQLException e) {
                response.put("error", "Erro ao inserir cliente: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        response.put("error", "Conexão com o banco falhou.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/show-client/{client_ID}")
    public ResponseEntity<List<Map<String, Object>>> getClientInfo(@PathVariable String client_ID) throws SQLException {
        String sql = "SELECT * FROM clients WHERE client_id=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, client_ID);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
                List<Map<String, Object>> clientsList = new ArrayList<>();
                Map<String, Object> clientMap = new HashMap<>();
                clientMap.put("client_id", rs.getString("client_id"));
                clientMap.put("client_name", rs.getString("client_name"));
                clientMap.put("client_address", rs.getString("client_address"));
                clientMap.put("client_CPF", rs.getString("client_CPF"));
                clientMap.put("client_type", rs.getString("client_type"));
                clientMap.put("client_CEP", rs.getString("client_CEP"));
                clientMap.put("client_activity", rs.getBoolean("client_activity"));
                clientsList.add(clientMap);
            return ResponseEntity.ok(clientsList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/show-clients")
    public ResponseEntity<List<Map<String, Object>>> getClientsInfos() {
        List<Map<String, Object>> clientsList = new ArrayList<>();

        try (
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clients");
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> clientMap = new HashMap<>();
                clientMap.put("client_id", rs.getString("client_id"));
                clientMap.put("client_name", rs.getString("client_name"));
                clientMap.put("client_address", rs.getString("client_address"));
                clientMap.put("client_CPF", rs.getString("client_CPF"));
                clientMap.put("client_type", rs.getString("client_type"));
                clientMap.put("client_CEP", rs.getString("client_CEP"));
                clientMap.put("client_activity", rs.getBoolean("client_activity"));
                clientsList.add(clientMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(clientsList);  // Retorna a lista com os dados
    }


    @PutMapping("/update-client/{client_ID}")
    public ResponseEntity<String> updateClientInfos(@PathVariable String client_ID, @RequestBody Client client) throws SQLException {
        try {
            if (client.getClient_CEP().length() == 8) {
                if (client.getClient_CPF().length() == 11) {
                    String sql = "UPDATE clients SET client_name=?, client_address=?, client_CPF=?, client_type=?, client_CEP=?,client_activity=? WHERE client_id=?";
                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, client.getClient_name());
                    pstmt.setString(2, client.getClient_address());
                    pstmt.setString(3, client.getClient_CPF());
                    pstmt.setString(4, client.isClient_type());
                    pstmt.setString(5, client.getClient_CEP());
                    pstmt.setBoolean(6, true);
                    pstmt.setString(7, client_ID);

                    pstmt.executeUpdate();
                    return ResponseEntity.ok("Cliente atualizado com sucesso!");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF com dígitos errados!");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CEP com dígitos errados!");
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/delete-client/{client_ID}")
    public ResponseEntity<Map<String, Object>> deleteClientInfos(@PathVariable String client_ID) throws SQLException {


        String selectSql = "SELECT * FROM clients WHERE client_id=?";
        pstmt = conn.prepareStatement(selectSql);
        pstmt.setString(1, client_ID);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String updateSql = " UPDATE clients SET client_activity=? WHERE client_id=?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setBoolean(1, false);
            pstmt.setString(2, client_ID);
            pstmt.executeUpdate();

                Map<String, Object> clientMap = new HashMap<>();
            clientMap.put("client_id", rs.getString("client_id"));
            clientMap.put("client_name", rs.getString("client_name"));
            clientMap.put("client_address", rs.getString("client_address"));
            clientMap.put("client_CPF", rs.getString("client_CPF"));
            clientMap.put("client_CEP", rs.getString("client_CEP"));
            clientMap.put("client_activity", rs.getBoolean("client_activity"));
            clientMap.put("client_type", rs.getString("client_type"));

                return ResponseEntity.ok(clientMap);
        }

        else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}