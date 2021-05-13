SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Table usuario
-- -----------------------------------------------------
DROP TABLE IF EXISTS usuario;
CREATE TABLE IF NOT EXISTS usuario (
	idusuario INT NOT NULL AUTO_INCREMENT,
	email VARCHAR(45) NOT NULL,
	permiso TINYINT(1) NOT NULL,
	password VARCHAR(60) NOT NULL,
	nombre VARCHAR(45) NULL,
	primer_apellido VARCHAR(45) NULL,
	telefono VARCHAR(16) NULL,
	PRIMARY KEY (idusuario))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table tipo_espacio
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_espacio;
CREATE TABLE IF NOT EXISTS tipo_espacio (
	id_tipo_espacio INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	PRIMARY KEY (id_tipo_espacio))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table estado
-- -----------------------------------------------------
DROP TABLE IF EXISTS estado;
CREATE TABLE IF NOT EXISTS estado (
	idestado INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	PRIMARY KEY (idestado))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table ciudad
-- -----------------------------------------------------
DROP TABLE IF EXISTS ciudad;

CREATE TABLE IF NOT EXISTS ciudad (
	idciudad INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	estado_id INT NOT NULL,
	PRIMARY KEY (idciudad),
	FOREIGN KEY (estado_id) REFERENCES estado (idestado))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table direccion
-- -----------------------------------------------------
DROP TABLE IF EXISTS direccion;
CREATE TABLE IF NOT EXISTS direccion (
	iddireccion INT NOT NULL AUTO_INCREMENT,
	direccion VARCHAR(50) NOT NULL,
	direccion2 VARCHAR(45) NULL,
	ciudad_id INT NOT NULL,
	PRIMARY KEY (iddireccion),
	FOREIGN KEY (ciudad_id) REFERENCES ciudad (idciudad))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table espacio
-- -----------------------------------------------------
DROP TABLE IF EXISTS espacio;
CREATE TABLE IF NOT EXISTS espacio (
	idespacio INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	email VARCHAR(45) NULL,
	telefono VARCHAR(45) NULL,
	tipo_espacio_id INT NOT NULL,
	direccion_id INT NOT NULL,
	PRIMARY KEY (idespacio),
	FOREIGN KEY (tipo_espacio_id) REFERENCES tipo_espacio (id_tipo_espacio),
    FOREIGN KEY (direccion_id) REFERENCES direccion (iddireccion))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table tipo_planta
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_planta;
CREATE TABLE IF NOT EXISTS tipo_planta (
	id_tipo_planta INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(45) NOT NULL,
	PRIMARY KEY (id_tipo_planta))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table usuario_espacio
-- -----------------------------------------------------
DROP TABLE IF EXISTS usuario_espacio;
CREATE TABLE IF NOT EXISTS usuario_espacio (
	usuario_id INT NOT NULL,
	espacio_id INT NOT NULL,
	PRIMARY KEY (usuario_id, espacio_id),
	FOREIGN KEY (usuario_id) REFERENCES usuario (idusuario),
    FOREIGN KEY (espacio_id) REFERENCES espacio (idespacio))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table forestacion
-- -----------------------------------------------------
DROP TABLE IF EXISTS forestacion;
CREATE TABLE IF NOT EXISTS forestacion (
  idforestacion INT NOT NULL AUTO_INCREMENT,
  num_plantas INT NOT NULL,
  coordenadas POINT NOT NULL,
  tipo_planta_id INT NOT NULL,
  usuario_id INT NOT NULL,
  espacio_id INT NOT NULL,
  fecha DATE NOT NULL,
  PRIMARY KEY (idforestacion),
  FOREIGN KEY (tipo_planta_id)
  REFERENCES tipo_planta (id_tipo_planta),
  FOREIGN KEY (usuario_id , espacio_id)
  REFERENCES usuario_espacio (usuario_id , espacio_id))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
