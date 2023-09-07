package com.bolsadeideas.springboot.facturito.app.models.dao;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.facturito.app.models.entity.Cliente;




public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{

	List<Cliente> findAll();

	void save(Cliente cliente);

	void deleteById(Long id);

	Optional<Cliente> findById(Long id);

	@Query("select c from Cliente c left join fetch c.facturas f where c.id = ?1")
	public Cliente fetchByIdWithFacturas(Long id);

}
