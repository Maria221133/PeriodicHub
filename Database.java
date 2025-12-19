package application;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DATABASE = "mg9860_project";
    private static final String HOST = "bridgeman-vm.hws.edu";
    private static final int PORT = 3306;
    private static final String DBUSER = "mg9860";
    private static final String DBPASS = "losenburnsto";

    Connection connection;

    public void openConnection() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
        connection = DriverManager.getConnection(url, DBUSER, DBPASS);
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public List<Element> getElements() throws SQLException {
        List<Element> elements = new ArrayList<>();
        String sql = "SELECT * FROM ELEMENTS e " +
                "JOIN PROPERTIES p ON e.atomic_number = p.atomic_number " +
                "JOIN ELEMENT_TYPES et ON e.element_type_id = et.element_type_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                // Retrieve Element properties
                int atomicNumber = rs.getInt("e.atomic_number");
                String name = rs.getString("e.name");
                String symbol = rs.getString("e.symbol");
                double atomicMass = rs.getDouble("e.atomic_mass");
                int elementTypeId = rs.getInt("e.element_type_id");
                int group = rs.getInt("e.grupo");
                int period = rs.getInt("e.period");

                // Retrieve Properties
                double density = rs.getDouble("p.density");
                double meltingPoint = rs.getDouble("p.melting_point");
                double boilingPoint = rs.getDouble("p.boiling_point");
                String stateOfMatter = rs.getString("p.state_of_matter");

                //String electronConfiguration = rs.getString("et.electron_configuration");


                Element element = new Element(atomicNumber, elementTypeId, name, symbol, atomicMass, group, period);
                element.setDensity(density);
                element.setMeltingPoint(meltingPoint);
                element.setBoilingPoint(boilingPoint);
                element.setStateOfMatter(stateOfMatter);
                //element.setElectronConfiguration(electronConfiguration);

                elements.add(element);
            }
        }
        return elements;
    }

    public User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE username = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("email"),
                            rs.getString("role_id")
                    );
                }
            }
        }
        return null; 
    }


    public User getUserDetails(String username) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("email"),
                            rs.getString("role_id")
                    );
                }
            }
        }
        return null;
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM USERS WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void createUser(String username, String password, String email, String role) throws SQLException {
        String sql = "INSERT INTO USERS (username, password_hash, email, role_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);
            stmt.executeUpdate();
        }
    }

    public String getElementTypeName(int elementTypeId) throws SQLException {
        String sql = "SELECT name FROM ELEMENT_TYPES WHERE element_type_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, elementTypeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null; 
    }

    public List<ResearchPaper> getResearchPapers() throws SQLException {
        List<ResearchPaper> papers = new ArrayList<>();
        String sql = "SELECT * FROM PAPERS";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int paperId = rs.getInt("paper_id");
                int atomicNumber = rs.getInt("atomic_number");
                int userId = rs.getInt("user_id");
                String title = rs.getString("title");
                String author = rs.getString("authors"); 

                LocalDate publicationDate = rs.getDate("publication_date").toLocalDate();

                String abstractText = rs.getString("abstract");
                String url = rs.getString("url");

                ResearchPaper paper = new ResearchPaper(paperId, atomicNumber, userId, title, author, publicationDate, abstractText, url);
                papers.add(paper);
            }
        }
        return papers;
    }

    private int getUserId(String username) throws SQLException {
        String sql = "SELECT user_id FROM USERS WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        return -1; 
    }

    public void insertResearchPaper(int atomicNumber, String currentUser, String title, String authors, LocalDate publicationDate,
                                      String abstractText, String url) throws SQLException {

        int userId = getUserId(currentUser);

        String sql = "INSERT INTO PAPERS (atomic_number, user_id, title, author, publication_date, abstract, url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, atomicNumber);
            stmt.setInt(2, userId);
            stmt.setString(3, title);
            stmt.setString(4, authors);
            stmt.setDate(5, Date.valueOf(publicationDate)); 
            stmt.setString(6, abstractText);
            stmt.setString(7, url);

            stmt.executeUpdate();
        }
    }
}