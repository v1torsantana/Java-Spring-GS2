package com.vitor.java.gs2_spring.operations;

import com.mysql.cj.exceptions.StreamingNotifiable;
import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Client;
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
    private Map<Integer, Client> clients_database = new HashMap<>();

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
        return ResponseEntity.ok("Foi, mas não adicionou no sql se pá");
    }

    @GetMapping("/show-client/{client_ID}")
    public ResponseEntity<Client> getClientInfo(@PathVariable int client_ID) {
        Client client = clients_database.get(client_ID);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/show-clients")
    public ResponseEntity<List<Client>> getClientsInfos() throws SQLException {
        List<Client> clientList = new ArrayList<>();

        String contador = "SELECT COUNT(*) FROM clients";
        pstmt = conn.prepareStatement(contador);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            if (count == 0) {
                return ResponseEntity.noContent().build();
            }
        }

        String sql = "SELECT * FROM clients";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            Client client = new Client(
                    rs.getString("client_name"),
                    rs.getString("client_address"),
                    rs.getString("client_CPF"),
                    rs.getString("client_type"),
                    rs.getString("client_CEP"),
                    rs.getBoolean("client_activity")
            );
            clientList.add(client);
        }
        System.out.println(clientList);
        return ResponseEntity.ok(clientList);
    }



        @PutMapping("/update-client/{client_ID}")
        public ResponseEntity<String> updateClientInfos ( @PathVariable int client_ID, @RequestBody Client client){
            if (clients_database.containsKey(client_ID)) {
                Client existingClient = clients_database.get(client_ID);
                existingClient.setClient_name(client.getClient_name());
                existingClient.setClient_address(client.getClient_address());
                existingClient.setClient_CPF(client.getClient_CPF());
                existingClient.setClient_type(client.isClient_type());
                existingClient.setClient_CEP(client.getClient_CEP());
                existingClient.setClient_activity(client.isClient_activity());
                return ResponseEntity.ok("Cliente atualizado com sucesso!");
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/delete-client/{client_ID}")
        public ResponseEntity<String> deleteClientInfos ( @PathVariable int client_ID){
            if (clients_database.containsKey(client_ID)) {
                Client client = clients_database.get(client_ID);
                client.setClient_activity(false);
                return ResponseEntity.ok("Cliente removido com sucesso!");
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }