-- 1. Inserare Utilizatori (Admini) - Rămâne neschimbat
INSERT INTO users (username, password, role) 
VALUES ('admin1', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3ueOGcL.F1JzebzgHYh.v/dy.5.Z.5.', 'ROLE_EDITOR');

INSERT INTO users (username, password, role) 
VALUES ('admin2', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3ueOGcL.F1JzebzgHYh.v/dy.5.Z.5.', 'ROLE_EDITOR');

-- 2. Inserare Picturi (Cu ANUL ca integer)
INSERT INTO paintings (title, author, period, technique, material) 
VALUES ('Noapte înstelată', 'Vincent van Gogh', 1889, 'Ulei pe pânză', 'Pânză');

INSERT INTO paintings (title, author, period, technique, material) 
VALUES ('Mona Lisa', 'Leonardo da Vinci', 1503, 'Sfumato', 'Lemn de plop');

INSERT INTO paintings (title, author, period, technique, material) 
VALUES ('Persistența memoriei', 'Salvador Dali', 1931, 'Suprarealism', 'Pânză');

INSERT INTO paintings (title, author, period, technique, material) 
VALUES ('Sărutul', 'Gustav Klimt', 1908, 'Foiță de aur', 'Pânză');

INSERT INTO paintings (title, author, period, technique, material) 
VALUES ('Fata cu cercel de perlă', 'Johannes Vermeer', 1665, 'Ulei', 'Pânză');