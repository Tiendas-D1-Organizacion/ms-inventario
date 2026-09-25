package com.d1.inventario;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class InventarioApplication {
    public static void main(String[] args) { SpringApplication.run(InventarioApplication.class, args); }
}

@Data
@Entity
class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}

interface ProductoRepository extends JpaRepository<Producto, Long> {}

@RestController
@RequestMapping("/")
class InventarioController {
    private final ProductoRepository repo;
    public InventarioController(ProductoRepository repo) { this.repo = repo; }

    @PostMapping
    public Producto crear(@RequestBody Producto p) { return repo.save(p); }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PutMapping("/{id}/deducir")
    public Producto deducirStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        Producto p = repo.findById(id).orElseThrow();
        if(p.getStock() < cantidad) throw new RuntimeException("Stock insuficiente");
        p.setStock(p.getStock() - cantidad);
        return repo.save(p);
    }
}
