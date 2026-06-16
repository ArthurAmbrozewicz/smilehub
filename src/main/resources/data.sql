-- =============================================================================
-- SmileHub - dados iniciais (desenvolvimento)
-- Executado automaticamente após o Hibernate criar as tabelas (create-drop).
-- =============================================================================
--
-- Credenciais de acesso:
--   Administrador : admin@smilehub.com    / admin123
--   Dentista      : dentista@smilehub.com  / dentista123
--
-- Pacientes de exemplo (sem login no sistema):
--   Maria Oliveira (id 3) | João Santos (id 4)
-- =============================================================================

-- Usuários (herança JOINED)
INSERT INTO usuarios (id, dtype, nome, email, senha, ativo, data_criacao) VALUES
(1, 'ADMINISTRADOR', 'Administrador', 'admin@smilehub.com',
 '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(2, 'DENTISTA', 'Dr. Ana Silva', 'dentista@smilehub.com',
 '$2a$10$O0MI.w.uNaQvxFOFVz9rYe7DWOrTItqCNRSBfCIRkLyKaOHjo5rO2', 1, NOW()),
(3, 'PACIENTE', 'Maria Oliveira', 'maria.oliveira@email.com',
 '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(4, 'PACIENTE', 'João Santos', 'joao.santos@email.com',
 '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW());

INSERT INTO administradores (id) VALUES (1);

INSERT INTO dentistas (id, cpf, cro) VALUES
(2, '11111111111', 'CRO-SP 12345');

INSERT INTO pacientes (id, cpf, telefone) VALUES
(3, '22222222222', '(11) 98888-0001'),
(4, '33333333333', '(11) 98888-0002');

-- Especialidades e vínculo com dentista
INSERT INTO especialidade (id, nome, usuario_id, ativo, data_criacao) VALUES
(1, 'Ortodontia', 1, 1, NOW()),
(2, 'Endodontia', 1, 1, NOW()),
(3, 'Clínico Geral', 1, 1, NOW());

INSERT INTO dentista_especialidade (id, id_dentista, id_especialidade, data_cadastro) VALUES
(1, 2, 1, NOW()),
(2, 2, 3, NOW());

-- Serviços odontológicos
INSERT INTO servico (id, nome, valor, ativo, usuario_id) VALUES
(1, 'Consulta de avaliação', 150.00, 1, 1),
(2, 'Limpeza dental', 220.00, 1, 1),
(3, 'Restauração em resina', 350.00, 1, 1);

-- Motivos de cancelamento
INSERT INTO motivo_cancelamento (id, descricao, usuario_criacao, data_criacao) VALUES
(1, 'Paciente não compareceu', 1, NOW()),
(2, 'Reagendamento solicitado pelo paciente', 1, NOW());

-- Consultas
INSERT INTO consulta (
    id, id_paciente, id_dentista, id_usuario, descricao,
    data_inicio, data_fim, data_registro, status
) VALUES
(1, 3, 2, 2, 'Avaliação ortodôntica inicial',
 '2026-06-20 09:00:00', '2026-06-20 09:30:00', NOW(), 'AGENDADA'),
(2, 3, 2, 2, 'Limpeza e revisão do odontograma',
 '2026-06-05 14:00:00', '2026-06-05 14:45:00', NOW(), 'FINALIZADA'),
(3, 4, 2, 1, 'Primeira consulta - clínico geral',
 '2026-06-22 10:00:00', '2026-06-22 10:30:00', NOW(), 'AGENDADA');

INSERT INTO consulta_servico (id, id_consulta, id_servico, valor) VALUES
(1, 1, 1, 150.00),
(2, 2, 2, 220.00),
(3, 3, 1, 150.00);

-- Odontograma da Maria (paciente 3)
INSERT INTO odontograma (id, id_paciente, id_dentista, data_ultima_alteracao, observacoes) VALUES
(1, 3, 2, NOW(), 'Paciente com histórico de cárie em molares.');

INSERT INTO dente (id, id_odontograma, numero, status, observacoes) VALUES
(1, 1, 16, 'cariado', 'Cárie oclusal superficial'),
(2, 1, 26, 'restaurado', 'Restauração em resina realizada em 2024'),
(3, 1, 36, 'tratamento', 'Tratamento de canal em andamento'),
(4, 1, 11, 'higido', 'Sem alterações');

-- Prescrição da consulta finalizada (consulta 2)
INSERT INTO prescricao (id, id_consulta, observacoes, data_emissao) VALUES
(1, 2, 'Evitar alimentos muito quentes nas primeiras 24h.', NOW());

INSERT INTO medicamento (id, id_prescricao, nome, dosagem, frequencia, duracao) VALUES
(1, 1, 'Dipirona', '500mg', 'De 6 em 6 horas se dor', 'Por 3 dias'),
(2, 1, 'Clorexidina bucal', '0,12%', 'Bochechar após escovação', 'Por 7 dias');

-- Ajuste dos auto-incrementos
ALTER TABLE usuarios AUTO_INCREMENT = 100;
ALTER TABLE especialidade AUTO_INCREMENT = 100;
ALTER TABLE servico AUTO_INCREMENT = 100;
ALTER TABLE motivo_cancelamento AUTO_INCREMENT = 100;
ALTER TABLE consulta AUTO_INCREMENT = 100;
ALTER TABLE consulta_servico AUTO_INCREMENT = 100;
ALTER TABLE odontograma AUTO_INCREMENT = 100;
ALTER TABLE dente AUTO_INCREMENT = 100;
ALTER TABLE prescricao AUTO_INCREMENT = 100;
ALTER TABLE medicamento AUTO_INCREMENT = 100;
ALTER TABLE dentista_especialidade AUTO_INCREMENT = 100;
