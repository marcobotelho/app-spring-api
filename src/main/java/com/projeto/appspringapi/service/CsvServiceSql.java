package com.projeto.appspringapi.service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class CsvServiceSql {

    @Value("${csv.file.path.estados}")
    private String filePathEstados;

    @Value("${csv.file.path.municipios}")
    private String filePathMunicipios;

    private Connection connection;

    public CsvServiceSql(@Autowired DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void lerCsvEInserirDados() throws IOException, CsvValidationException, SQLException {
        long tempoInicio = System.currentTimeMillis();
        inserirDados(filePathEstados, "INSERT INTO ESTADO (ID, NOME, SIGLA) VALUES (?, ?, ?)");
        inserirDados(filePathMunicipios, "INSERT INTO MUNICIPIO (ESTADO_ID, ID, NOME) VALUES (?, ?, ?)");
        long tempoFim = System.currentTimeMillis();
        long tempoExecucao = tempoFim - tempoInicio;
        System.out.println("Tempo de execução: " + tempoExecucao + "ms");
    }

    private void inserirDados(String filePath, String query)
            throws IOException, CsvValidationException, SQLException {
        try (CSVReader csvReader = new CSVReader(
                new FileReader(filePath, StandardCharsets.UTF_8))) {
            csvReader.skip(1); // Skip the first line
            String[] linha;
            while ((linha = csvReader.readNext()) != null) {
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    for (int i = 0; i < linha.length; i++) {
                        ps.setString(i + 1, linha[i]);
                    }
                    ps.executeUpdate();
                }
            }
        }
    }

}
