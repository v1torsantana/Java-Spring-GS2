package com.vitor.java.gs2_spring.operations;

import com.mysql.cj.exceptions.StreamingNotifiable;
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
    public ResponseEntity<String> setClientInfos(@RequestBody Client client) throws SQLException {

        if (conn != null) {
            try {
                //FAZER CHECAGEM DE SE A PESSOA JÁ ESTÁ CADASTRADA
                String sql = "INSERT INTO clients (client_id, client_name, client_address, client_CPF, client_type, client_CEP, client_activity) VALUES (?, ?, ?, ?, ?, ?, ?)";
                String id = String.valueOf(client.getClientID());
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, id);
                pstmt.setString(2, client.getClient_name());
                pstmt.setString(3, client.getClient_address());
                pstmt.setString(4, client.getClient_CPF());
                pstmt.setString(5, client.isClient_type());
                pstmt.setString(6, client.getClient_CEP());
                pstmt.setBoolean(7, client.isClient_activity());
                pstmt.executeUpdate();

                return ResponseEntity.ok("Cliente adicionado com sucesso!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok("Sucesso");
    }

    @GetMapping("/show-client/{client_ID}")
    public ResponseEntity<Client> getClientInfo(@PathVariable String client_ID) throws SQLException {
        String sql = "SELECT * FROM clients WHERE client_id=?";
        Client client = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, client_ID);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            client = new Client(
                    rs.getString("client_name"),
                    rs.getString("client_address"),
                    rs.getString("client_CPF"),
                    rs.getString("client_type"),
                    rs.getString("client_CEP"),
                    rs.getBoolean("client_activity")
            );
            return ResponseEntity.ok(client);
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
        String sql = "UPDATE clients SET client_name=?, client_address=?, client_CPF=?, client_type=?, client_CEP=?,client_activity=? WHERE client_id=?";
        pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, client.getClient_name());
        pstmt.setString(2, client.getClient_address());
        pstmt.setString(3, client.getClient_CPF());
        pstmt.setString(4, client.isClient_type());
        pstmt.setString(5, client.getClient_CEP());
        pstmt.setBoolean(6, client.isClient_activity());
        pstmt.setString(7, client_ID);

        pstmt.executeUpdate();

        return ResponseEntity.ok("Cliente atualizado com sucesso!");
    }


    @DeleteMapping("/delete-client/{client_ID}")
    public ResponseEntity<String> deleteClientInfos(@PathVariable String client_ID) throws SQLException {
        String sql = "UPDATE clients SET client_activity=? WHERE client_id=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, false);
        pstmt.setString(2, client_ID);
        pstmt.executeUpdate();
        return ResponseEntity.ok("Cliente desativado com sucesso!");
    }
}