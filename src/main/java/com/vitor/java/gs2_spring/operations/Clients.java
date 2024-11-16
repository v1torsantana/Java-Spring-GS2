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
public class Clients{
    private Map<Integer, Client> clients_database = new HashMap<>();

    @PostMapping("/register-clients")
    public ResponseEntity<String> setClientInfos(@RequestBody Client client){
        String clientIDString = String.valueOf(client.getClientID());
        if(clientIDString.length()==5) {
            if(client.getClient_CPF().length()==11) {
                if(client.getClient_CEP().length()==8) {
                    clients_database.put(client.getClientID(), client);
                    return ResponseEntity.ok("Cliente adicionado com sucesso!");
                }
                else{
                    return ResponseEntity.badRequest().body("O CEP deve ter 8 dígitos");
                }
            }
            else{
                return ResponseEntity.badRequest().body("O CPF deve ter 11 dígitos");
            }
        }
        else{
            return ResponseEntity.badRequest().body("O ID do Cliente deve ter 5 dígitos");
        }
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
    public ResponseEntity<String> updateClientInfos(
            @PathVariable int client_ID,
            @RequestBody Client client) {

        String clientIDString = String.valueOf(client.getClientID());
        if (clientIDString.length() != 5) {
            return ResponseEntity.badRequest().body("O ID do cliente deve ter 5 dígitos.");
        }

        if (client.getClient_CPF().length() != 11) {
            return ResponseEntity.badRequest().body("O CPF deve ter 11 dígitos.");
        }

        if (client.getClient_CEP().length() != 8) {
            return ResponseEntity.badRequest().body("O CEP deve ter 8 dígitos.");
        }

        if (!clients_database.containsKey(client_ID)) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza os campos do cliente
        Client existingClient = clients_database.get(client_ID);
        existingClient.setClient_CPF(client.getClient_CPF());
        existingClient.setClient_CEP(client.getClient_CEP());
        existingClient.setClient_activity(client.isClient_activity());
        existingClient.setClient_address(client.getClient_address());
        existingClient.setClient_type(client.getClient_type());
        existingClient.setClient_name(client.getClient_name());

        // Substitui no banco de dados
        clients_database.put(client_ID, existingClient);

        return ResponseEntity.ok("Cliente atualizado com sucesso!");
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
