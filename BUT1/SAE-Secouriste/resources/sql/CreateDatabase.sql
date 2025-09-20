-- Création de la base
CREATE DATABASE IF NOT EXISTS sae_db;
USE sae_db;

-- Suppression des tables si elles existent déjà (ordre inverse)
DROP TABLE IF EXISTS Certificat;
DROP TABLE IF EXISTS Affectation;
DROP TABLE IF EXISTS Necessite;
DROP TABLE IF EXISTS Possession;
DROP TABLE IF EXISTS Disponibilite;
DROP TABLE IF EXISTS Besoin;
DROP TABLE IF EXISTS Notification;
DROP TABLE IF EXISTS DPS;
DROP TABLE IF EXISTS Competence;
DROP TABLE IF EXISTS Journee;
DROP TABLE IF EXISTS Sport;
DROP TABLE IF EXISTS Site;
DROP TABLE IF EXISTS Administrateur;
DROP TABLE IF EXISTS Secouriste;
DROP TABLE IF EXISTS User;


-- Table des sites
CREATE TABLE Site (
    code INTEGER,
    nom VARCHAR(64) NOT NULL,
    latitude FLOAT,
    longitude FLOAT,
    CONSTRAINT pk_Site PRIMARY KEY (code)
);

-- Table des utilisateurs
CREATE TABLE User (
    idUser INTEGER AUTO_INCREMENT UNIQUE,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL,
    CONSTRAINT pk_User PRIMARY KEY (idUser),
    CONSTRAINT ck_User CHECK (login LIKE '%@%.%')
);

-- Table des secouristes
CREATE TABLE Secouriste (
    idSecouriste INTEGER,
    nom VARCHAR(32) NOT NULL,
    prenom VARCHAR(32) NOT NULL,
    date_naissance VARCHAR(32),
    tel VARCHAR(10),
    adresse VARCHAR(64),
    photo LONGBLOB,
    CONSTRAINT pk_Secouriste PRIMARY KEY (idSecouriste),
    CONSTRAINT fk_Secouriste_User FOREIGN KEY (idSecouriste) REFERENCES User(idUser)
);

-- Table des administrateurs
CREATE TABLE Administrateur (
    idAdministrateur INTEGER,
    nom VARCHAR(32) NOT NULL,
    prenom VARCHAR(32) NOT NULL,
    date_naissance VARCHAR(32),
    tel VARCHAR(10),
    adresse VARCHAR(64),
    photo LONGBLOB,
    CONSTRAINT pk_Administrateur PRIMARY KEY (idAdministrateur),
    CONSTRAINT fk_Administrateur_User FOREIGN KEY (idAdministrateur) REFERENCES User(idUser)
);

-- Table des sports
CREATE TABLE Sport (
    code INTEGER,
    nom VARCHAR(64) NOT NULL,
    CONSTRAINT pk_Sport PRIMARY KEY (code)
);

-- Table des journées
CREATE TABLE Journee (
    id INTEGER AUTO_INCREMENT,
    jour INT NOT NULL,
    mois INT NOT NULL,
    annee INT NOT NULL,
    CONSTRAINT pk_Journee PRIMARY KEY (id)
);

-- Table des compétences
CREATE TABLE Competence (
    intitule VARCHAR(64),
    CONSTRAINT pk_Competence PRIMARY KEY (intitule)
);

-- Table des DPS
CREATE TABLE DPS (
    id INTEGER,
    name VARCHAR(100) NOT NULL,
    horaire_depart INTEGER,
    horaire_fin INTEGER,
    site INTEGER,
    sport INTEGER,
    journee INTEGER,
    CONSTRAINT uq_name UNIQUE(name),
    CONSTRAINT pk_DPS PRIMARY KEY (id),
    CONSTRAINT fk_DPS_Site FOREIGN KEY (site) REFERENCES Site(code),
    CONSTRAINT fk_DPS_Sport FOREIGN KEY (sport) REFERENCES Sport(code),
    CONSTRAINT fk_DPS_Journee FOREIGN KEY (journee) REFERENCES Journee(id)
);

-- Table des besoins (liée à DPS)
CREATE TABLE Besoin (
    id INTEGER AUTO_INCREMENT,
    dps INTEGER,
    competence VARCHAR(64),
    CONSTRAINT pk_Besoin PRIMARY KEY (id),
    CONSTRAINT fk_Besoin_Competence FOREIGN KEY (competence) REFERENCES Competence(intitule),
    CONSTRAINT fk_Besoin_DPS FOREIGN KEY (dps) REFERENCES DPS(id)
);

-- Table des disponibilités
CREATE TABLE Disponibilite (
    secouristeDisp INTEGER,
    journeeDisp INTEGER,
    CONSTRAINT pk_Disponibilite PRIMARY KEY (secouristeDisp, journeeDisp),
    CONSTRAINT fk_Disponibilite_Secouriste FOREIGN KEY (secouristeDisp) REFERENCES Secouriste(idSecouriste),
    CONSTRAINT fk_Disponibilite_Journee FOREIGN KEY (journeeDisp) REFERENCES Journee(id)
);

-- Table des possessions
CREATE TABLE Possession (
    secouriste INTEGER,
    competence VARCHAR(64),
    CONSTRAINT pk_Possession PRIMARY KEY (secouriste, competence),
    CONSTRAINT fk_Possession_Secouriste FOREIGN KEY (secouriste) REFERENCES Secouriste(idSecouriste),
    CONSTRAINT fk_Possession_Competence FOREIGN KEY (competence) REFERENCES Competence(intitule)
);

-- Table des nécessités
CREATE TABLE Necessite (
    comp1 VARCHAR(64),
    comp2 VARCHAR(64),
    CONSTRAINT pk_Necessite PRIMARY KEY (comp1, comp2),
    CONSTRAINT fk_Necessite_Competence1 FOREIGN KEY (comp1) REFERENCES Competence(intitule),
    CONSTRAINT fk_Necessite_Competence2 FOREIGN KEY (comp2) REFERENCES Competence(intitule)
);

-- Table des affectations
CREATE TABLE Affectation (
    secouristeAffect INTEGER,
    DPSAffect INTEGER,
    competenceAffect VARCHAR(64),
    CONSTRAINT pk_Affectation PRIMARY KEY (secouristeAffect, DPSAffect, competenceAffect),
    CONSTRAINT fk_Affectation_Secouriste FOREIGN KEY (secouristeAffect) REFERENCES Secouriste(idSecouriste),
    CONSTRAINT fk_Affectation_DPS FOREIGN KEY (DPSAffect) REFERENCES DPS(id),
    CONSTRAINT fk_Affectation_Competence FOREIGN KEY (competenceAffect) REFERENCES Competence(intitule)
);

-- Table Notifications (pour les notifications des affectations)
CREATE TABLE Notification (
    title VARCHAR(20) NOT NULL,
    message VARCHAR(255) NOT NULL,
    date VARCHAR(20),
    sender INTEGER,
    recipient INTEGER,
    idDPS INTEGER,
    isViewed BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_Notification PRIMARY KEY (date, sender, recipient),
    CONSTRAINT fk_Notification_Sender FOREIGN KEY (sender) REFERENCES Administrateur(idAdministrateur),
    CONSTRAINT fk_Notification_Recipient FOREIGN KEY (recipient) REFERENCES User(idUser),
    CONSTRAINT fk_Notification_idDPS FOREIGN KEY (idDPS) REFERENCES DPS(id) ON DELETE CASCADE
);
