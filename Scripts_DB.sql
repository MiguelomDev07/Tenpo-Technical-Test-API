
-- ****************************** TABLA: Historial de Llamadas
CREATE TABLE requesthistory (
  id integer not null,
  endpoint VARCHAR(50) not null,
  parameters VARCHAR(31) not null,
  response VARCHAR(100) not null,
  creationdate DATE not null
);

-- select * from requesthistory;

-- truncate table requesthistory;