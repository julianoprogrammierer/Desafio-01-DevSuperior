package com.example.challenger.services;

import com.example.challenger.dto.ClientDTO;
import com.example.challenger.entities.Client;
import com.example.challenger.exceptions.ResourceNotFoundException;
import com.example.challenger.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> list=repository.findAll(pageRequest);
        return list.map(elem->new ClientDTO(elem));
    }

    public ClientDTO getById(Long id){
        Optional<Client> obj=repository.findById(id);
        Client client=obj.orElseThrow(()-> new ResourceNotFoundException("Entity not Found!"));
        return new ClientDTO(client);
    }

    public ClientDTO insert(ClientDTO dto){
        Client client=new Client();
        client=dtoToEntity(dto,client);
        client=repository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(ClientDTO dto, Long id){
        try{
            Client client=repository.getOne(id);
            client=dtoToEntity(dto,client);
            client=repository.save(client);
            return new ClientDTO(client);

        }catch (EntityNotFoundException e) {

            throw  new ResourceNotFoundException("Entidade de id: "+id+
                    " não encontrado. Elemento "+dto.toString()+
                    " não foi possível atualizar");
         }

    }

    public void delete(Long id){

        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){

            throw  new ResourceNotFoundException("Entity id:" + id + " not found!");

        }catch (DataIntegrityViolationException e){

            throw  new ResourceNotFoundException("Data Integrate Violation" + e.getMessage());
        }



    }

    private Client dtoToEntity(ClientDTO dto, Client client){
        client.setName(dto.getName());
        client.setCpf(dto.getCpf());
        client.setIncome(dto.getIncome());
        client.setBirthDate(dto.getBirthDate());
        client.setChildren(dto.getChildren());
        return client;
    }


}
