package com.example.sistemaempleados.repositories.proyecto;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("postgres")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProyectoRepositoryPostgreSQLTest extends ProyectoRepositoryTest {
}
