package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.settings.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@Service
public class Clients {
    private Map<Integer, Client> clients_database = new HashMap<>();

    @PostMapping("/register-clients")
    public ResponseEntity<String> setClientInfos(@RequestBody Client client){
        clients_database.put(client.getClientID(), client);
        return ResponseEntity.ok("Cliente adicionado com sucesso!");
    }

    @GetMapping("/show-client/{client_ID}")
    public ResponseEntity<Client> getClientInfo(@PathVariable int client_ID){
        Client client = clients_database.get(client_ID);
        if(client != null){
            return ResponseEntity.ok(client);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/show-clients")
    public ResponseEntity<List<Client>> getClientsInfos(){
        if(clients_database.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            List<Client> clientList = new ArrayList<>(clients_database.values());
            return ResponseEntity.ok(clientList);
        }
    }

    @PutMapping("/update-client/{client_ID}")
    public ResponseEntity<String> updateClientInfos(@PathVariable int client_ID, @RequestBody Client client){
        if(clients_database.containsKey(client_ID)){
            clients_database.put(client_ID, client);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-client/{client_ID}")
    public ResponseEntity<String> deleteClientInfos(@PathVariable int client_ID){
        if(clients_database.containsKey(client_ID)){
            clients_database.remove(client_ID);
            return ResponseEntity.ok("Cliente removido com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
