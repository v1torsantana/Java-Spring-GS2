package com.vitor.java.gs2_spring.settings;

import com.vitor.java.gs2_spring.settings.operations.Client;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@Service
public class Clients {
    private Map<Integer, Client> client_database = new HashMap<>();

    // colocar o metodo POST
    @PostMapping("/register-client")
    public ResponseEntity<String> setClientsInfos(@RequestBody Client client){
        client_database.put(client.getClientID(), client);
        return ResponseEntity.ok("Cliente registrado com sucesso");
    }

    // colocar o metodo GET
    @GetMapping("/show-clients/{clientID}")
    public ResponseEntity<Client> getClientsInfos(@PathVariable int client_ID){
        Client client = client_database.get(client_ID);
        if (client!= null){
            return ResponseEntity.ok(client);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    // colocar o metodo PUT
    @PutMapping("/update-clients")
    public ResponseEntity<String> updateClientsInfos(@PathVariable int client_ID, @RequestBody Client client){
        if (client_database.containsKey(client_ID)){
            client_database.put(client_ID, client);
            return ResponseEntity.ok("Cliente atualizado com sucesso");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    // colocar o metodo DELETE
    @DeleteMapping("/delete-clients")
    public ResponseEntity<String> deleteClientsInfos(@PathVariable int client_ID){
        //Criar o metodo de deletar o cliente !!!!!!!!!!!!
        boolean deleted = true;
        if (client_database.containsKey(client_ID)){
            client_database.remove(client_ID);
            return ResponseEntity.ok("Cliente deletado com sucesso");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


}
