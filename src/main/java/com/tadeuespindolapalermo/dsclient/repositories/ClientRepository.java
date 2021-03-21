package com.tadeuespindolapalermo.dsclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tadeuespindolapalermo.dsclient.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
