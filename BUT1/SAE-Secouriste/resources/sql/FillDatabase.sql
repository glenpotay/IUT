-- Nettoyage comme avant
USE sae_db;

DELETE FROM Affectation;
DELETE FROM Necessite;
DELETE FROM Possession;
DELETE FROM Disponibilite;
DELETE FROM Besoin;
DELETE FROM DPS;
DELETE FROM Competence;
DELETE FROM Journee;
DELETE FROM Sport;
DELETE FROM Administrateur;
DELETE FROM Secouriste;
DELETE FROM User;
DELETE FROM Site;
ALTER TABLE User AUTO_INCREMENT = 1;

-- Insertion des vrais sites JO 2030 Alpes françaises
-- Coordonnées GPS réelles des sites olympiques
INSERT INTO Site (code, nom, latitude, longitude) VALUES
                                                      (1, 'La Clusaz', 45.9044, 6.4231),
                                                      (2, 'Le Grand-Bornand', 45.9403, 6.4278),
                                                      (3, 'Courchevel', 45.4147, 6.6342),
                                                      (4, 'Méribel', 45.3864, 6.5658),
                                                      (5, 'Val d''Isère', 45.4487, 6.9792),
                                                      (6, 'La Plagne', 45.5547, 6.6797),
                                                      (7, 'Serre-Chevalier', 44.9428, 6.5506),
                                                      (8, 'Montgenèvre', 44.9311, 6.7275),
                                                      (9, 'Isola 2000', 44.1842, 6.9719),
                                                      (10, 'Nice', 43.7102, 7.2620),
                                                      (11, 'Briançon', 44.8978, 6.6408);

-- Sports JO d'hiver 2030 (disciplines olympiques réelles)
INSERT INTO Sport (code, nom) VALUES
                                  (1, 'Ski alpin'),
                                  (2, 'Ski nordique'),
                                  (3, 'Biathlon'),
                                  (4, 'Saut à ski'),
                                  (5, 'Combiné nordique'),
                                  (6, 'Ski freestyle'),
                                  (7, 'Snowboard'),
                                  (8, 'Patinage artistique'),
                                  (9, 'Patinage de vitesse'),
                                  (10, 'Hockey sur glace'),
                                  (11, 'Curling'),
                                  (12, 'Bobsleigh'),
                                  (13, 'Skeleton'),
                                  (14, 'Luge'),
                                  (15, 'Escalade');

-- Journées pour les JO 2030 (du 8 au 24 février)
INSERT INTO Journee (jour, mois, annee) VALUES (8, 2, 2030);
SET @idJournee1 = LAST_INSERT_ID();
INSERT INTO Journee (jour, mois, annee) VALUES (15, 2, 2030);
SET @idJournee2 = LAST_INSERT_ID();
INSERT INTO Journee (jour, mois, annee) VALUES (22, 2, 2030);
SET @idJournee3 = LAST_INSERT_ID();

-- Compétences (identiques à l'original)
INSERT INTO Competence (intitule) VALUES
                                      ('PSE1'), ('PSE2'), ('CE'), ('CP'), ('CO'), ('SSA'), ('VPSP'), ('PBC'), ('PBF');

-- Graphe des nécessités (identique à l'original)
INSERT INTO Necessite VALUES ('PSE1', 'PSE2');
INSERT INTO Necessite VALUES ('PSE2', 'CE');
INSERT INTO Necessite VALUES ('CE', 'CP');
INSERT INTO Necessite VALUES ('CP', 'CO');
INSERT INTO Necessite VALUES ('SSA', 'PSE1');
INSERT INTO Necessite VALUES ('VPSP', 'PSE2');
INSERT INTO Necessite VALUES ('PBF', 'PBC');

INSERT INTO User (login, password, role) VALUES
                                             ('admin@secouriste.fr', '$2a$12$kd.N0.jMwDU8lPta7XBt2OneFnJn.rwUSlUV4Esli4hm5WuGnK2uK', 'administrator'),
                                             ('sec1@jo2030.fr', '$2a$12$XeQoOhvEDt6usRMrJJ6eDO7zx4Jd85hoDRCyk7VYzNncRoDE9jEtG', 'rescuer'),
                                             ('sec2@jo2030.fr', '$2a$12$gyrUBh43ZLkuoX0Z/KBtN.VR70yUxrZIMSVHMMbxdHNnIDQ3AwCp6', 'rescuer'),
                                             ('sec3@jo2030.fr', '$2a$12$/GZ1VSzSL2Xt564XSiM0q.laOANnc3zOisV.4f3ziMjzMfPBjt2vC', 'rescuer'),
                                             ('sec4@jo2030.fr', '$2a$12$MatsrruuEXbWMnUDQgXUauAwxMT/cKDBP1Yhxyeg.qZB0a8v/Qw8i', 'rescuer'),
                                             ('sec5@jo2030.fr', '$2a$12$VB0RIcWcPiJzqMWFinUl1e8P1fxIU6geN2FdwpNADq4oxPsi03d6m', 'rescuer'),
                                             ('sec6@jo2030.fr', '$2a$12$QUyO9EJgmDReADmR2KY8x.aZZ/3EaPlbWfhdivAccAcqkm5GpXqoi', 'rescuer'),
                                             ('sec7@jo2030.fr', '$2a$12$AjAYlwnmXwbUx4yiacdNn.P6k30cUx0CDR3acQZ5LMrCtJ0SeFg5a', 'rescuer'),
                                             ('sec8@jo2030.fr', '$2a$12$BsKa4Nh5KdRLT5ExTIt39.0qvyo3HERGEPaUXRVDHvHjsGiB2rjfi', 'rescuer'),
                                             ('sec9@jo2030.fr', '$2a$12$NhYxnZORtwhE.UqLD8gfWu0wqgjdUuGBtAQZUiIRI4gNz//pzD9g2', 'rescuer'),
                                             ('sec10@jo2030.fr', '$2a$12$MVREpYB08j4SyWnPbfWPSOsfghsYMM9ZN8XTX210SytS0uyD620ZS', 'rescuer'),
                                             ('sec11@jo2030.fr', '$2a$12$MzTaci45ecQlYvANfQr.welG884Im/7EFkwnWfbgwpG5E9SL0IMB6', 'rescuer'),
                                             ('sec12@jo2030.fr', '$2a$12$AlY7Jct8vvZvzfTJQevDPOTEYf50EwiP68qbaJ/r43yKkibtC6Tca', 'rescuer');

-- Récupération des IDs User insérés
SELECT @idSec1 := idUser FROM User WHERE login = 'sec1@jo2030.fr';
SELECT @idSec2 := idUser FROM User WHERE login = 'sec2@jo2030.fr';
SELECT @idSec3 := idUser FROM User WHERE login = 'sec3@jo2030.fr';
SELECT @idSec4 := idUser FROM User WHERE login = 'sec4@jo2030.fr';
SELECT @idSec5 := idUser FROM User WHERE login = 'sec5@jo2030.fr';
SELECT @idSec6 := idUser FROM User WHERE login = 'sec6@jo2030.fr';
SELECT @idSec7 := idUser FROM User WHERE login = 'sec7@jo2030.fr';
SELECT @idSec8 := idUser FROM User WHERE login = 'sec8@jo2030.fr';
SELECT @idSec9 := idUser FROM User WHERE login = 'sec9@jo2030.fr';
SELECT @idSec10 := idUser FROM User WHERE login = 'sec10@jo2030.fr';
SELECT @idSec11 := idUser FROM User WHERE login = 'sec11@jo2030.fr';
SELECT @idSec12 := idUser FROM User WHERE login = 'sec12@jo2030.fr';

-- Insertion dans Secouriste avec profils réalistes pour JO 2030
INSERT INTO Secouriste (idSecouriste, nom, prenom, date_naissance, tel, adresse) VALUES
                                                                                     (@idSec1, 'Martin', 'Pierre', '1985-03-15', '0645123456', '12 rue des Alpes, 74220 La Clusaz'),
                                                                                     (@idSec2, 'Durand', 'Sophie', '1990-07-22', '0656234567', '25 avenue Mont-Blanc, 74450 Le Grand-Bornand'),
                                                                                     (@idSec3, 'Moreau', 'Lucas', '1988-11-08', '0667345678', '8 impasse des Neiges, 73120 Courchevel'),
                                                                                     (@idSec4, 'Bernard', 'Emma', '1992-01-30', '0678456789', '15 chemin des Pistes, 73550 Méribel'),
                                                                                     (@idSec5, 'Petit', 'Julien', '1987-05-12', '0689567890', '3 rue de la Montagne, 73150 Val d''Isère'),
                                                                                     (@idSec6, 'Robert', 'Camille', '1991-09-25', '0690678901', '22 route Olympique, 73210 La Plagne'),
                                                                                     (@idSec7, 'Richard', 'Thomas', '1986-12-03', '0601789012', '7 place des Écrins, 05240 Serre-Chevalier'),
                                                                                     (@idSec8, 'Garcia', 'Léa', '1993-04-18', '0612890123', '14 avenue des Sports, 05100 Montgenèvre'),
                                                                                     (@idSec9, 'Martinez', 'Antoine', '1989-08-07', '0623901234', '9 chemin Isola, 06420 Isola 2000'),
                                                                                     (@idSec10, 'Lopez', 'Marine', '1994-02-14', '0634012345', '18 promenade des Anglais, 06000 Nice'),
                                                                                     (@idSec11, 'Gonzalez', 'Maxime', '1990-06-21', '0645123456', '11 rue Vauban, 05100 Briançon'),
                                                                                     (@idSec12, 'Perez', 'Clara', '1988-10-09', '0656234567', '5 boulevard des Alpes, 73000 Chambéry');

-- Administrateur
SELECT @idAdm1 := idUser FROM User WHERE login = 'admin@secouriste.fr';
INSERT INTO Administrateur (idAdministrateur, nom, prenom, date_naissance, tel, adresse) VALUES
    (@idAdm1, 'Dupont', 'Martin', '1975-03-12', '0645783219', 'COJO Alpes Françaises 2030, Lyon, France');

-- Disponibilités réparties sur les trois journées clés des JO
INSERT INTO Disponibilite VALUES
                              (@idSec1, @idJournee1), (@idSec1, @idJournee2), (@idSec1, @idJournee3),
                              (@idSec2, @idJournee1), (@idSec2, @idJournee2),
                              (@idSec3, @idJournee1), (@idSec3, @idJournee3),
                              (@idSec4, @idJournee2), (@idSec4, @idJournee3),
                              (@idSec5, @idJournee1), (@idSec5, @idJournee2),
                              (@idSec6, @idJournee2), (@idSec6, @idJournee3),
                              (@idSec7, @idJournee1), (@idSec7, @idJournee3),
                              (@idSec8, @idJournee1), (@idSec8, @idJournee2), (@idSec8, @idJournee3),
                              (@idSec9, @idJournee2), (@idSec9, @idJournee3),
                              (@idSec10, @idJournee1), (@idSec10, @idJournee2),
                              (@idSec11, @idJournee1), (@idSec11, @idJournee3),
                              (@idSec12, @idJournee2), (@idSec12, @idJournee3);

-- Possessions compétences respectant le graphe des prérequis
-- Secouristes spécialisés selon leur affectation prévue
INSERT INTO Possession VALUES
                           -- Secouriste 1 (La Clusaz) - Compétences avancées
                           (@idSec1, 'PSE1'), (@idSec1, 'PSE2'), (@idSec1, 'CE'), (@idSec1, 'SSA'),

                           -- Secouriste 2 (Le Grand-Bornand) - PSE2 + spécialisation
                           (@idSec2, 'PSE1'), (@idSec2, 'PSE2'), (@idSec2, 'VPSP'),

                           -- Secouriste 3 (Courchevel) - Chef d'équipe
                           (@idSec3, 'PSE1'), (@idSec3, 'PSE2'), (@idSec3, 'CE'), (@idSec3, 'CP'),

                           -- Secouriste 4 (Méribel) - Coordinateur
                           (@idSec4, 'PSE1'), (@idSec4, 'PSE2'), (@idSec4, 'CE'), (@idSec4, 'CP'), (@idSec4, 'CO'),

                           -- Secouriste 5 (Val d'Isère) - PSE2 + SSA
                           (@idSec5, 'PSE1'), (@idSec5, 'PSE2'), (@idSec5, 'SSA'),

                           -- Secouriste 6 (La Plagne) - Spécialiste piste
                           (@idSec6, 'PSE1'), (@idSec6, 'PSE2'), (@idSec6, 'VPSP'),

                           -- Secouriste 7 (Serre-Chevalier) - Base nautique
                           (@idSec7, 'PBC'), (@idSec7, 'PBF'),

                           -- Secouriste 8 (Montgenèvre) - PSE1 + SSA
                           (@idSec8, 'PSE1'), (@idSec8, 'SSA'),

                           -- Secouriste 9 (Isola 2000) - PSE2 + Base nautique
                           (@idSec9, 'PSE1'), (@idSec9, 'PSE2'), (@idSec9, 'PBC'),

                           -- Secouriste 10 (Nice) - Base nautique complète
                           (@idSec10, 'PBC'), (@idSec10, 'PBF'),

                           -- Secouriste 11 (Briançon) - Chef d'équipe
                           (@idSec11, 'PSE1'), (@idSec11, 'PSE2'), (@idSec11, 'CE'),

                           -- Secouriste 12 (Chambéry) - Coordinateur général
                           (@idSec12, 'PSE1'), (@idSec12, 'PSE2'), (@idSec12, 'CE'), (@idSec12, 'CP'), (@idSec12, 'CO');
