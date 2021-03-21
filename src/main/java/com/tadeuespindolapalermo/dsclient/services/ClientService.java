package com.tadeuespindolapalermo.dsclient.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tadeuespindolapalermo.dsclient.dto.ClientDTO;
import com.tadeuespindolapalermo.dsclient.entities.Client;
import com.tadeuespindolapalermo.dsclient.repositories.ClientRepository;
import com.tadeuespindolapalermo.dsclient.services.exceptions.DatabaseException;
import com.tadeuespindolapalermo.dsclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest) {
		return repository.findAll(pageRequest).map(ClientDTO::new);
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		return new ClientDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		return new ClientDTO(repository.save(copyDtoToEntity(dto, new Client())));
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			return new ClientDTO(repository.save(copyDtoToEntity(dto, repository.getOne(id))));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private Client copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		return entity;
	}

}
