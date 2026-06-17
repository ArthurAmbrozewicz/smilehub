-- =============================================================================
-- SmileHub - Dados Iniciais de Teste e Desenvolvimento (Super Preenchido)
-- Executado automaticamente após o Hibernate criar as tabelas (create-drop).
-- =============================================================================
--
-- Credenciais de acesso atualizadas:
--   Administrador : admin@smilehub.com      / admin123
--   Dentista 1    : dentista@smilehub.com    / dentista123
--   Dentista 2    : carlos.orto@smilehub.com / carlos123
--   Dentista 3    : juliana.endo@smilehub.com/ juliana123
--
-- Pacientes de exemplo (ids 4 a 12)
-- =============================================================================

-- =============================================================================
-- 1. USUÁRIOS (Estratégia de Herança JOINED)
-- Senhas criptografadas com BCrypt correspondentes aos exemplos acima
-- =============================================================================
INSERT INTO usuarios (id, dtype, nome, email, senha, ativo, data_criacao) VALUES
-- Administrador (id 1)
(1, 'ADMINISTRADOR', 'Administrador do Sistema', 'admin@smilehub.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),

-- Dentistas (ids 2, 3, 4)
(2, 'DENTISTA', 'Dr. Ana Silva', 'dentista@smilehub.com', '$2a$10$O0MI.w.uNaQvxFOFVz9rYe7DWOrTItqCNRSBfCIRkLyKaOHjo5rO2', 1, NOW()),
(3, 'DENTISTA', 'Dr. Carlos Roberto', 'carlos.orto@smilehub.com', '$2a$10$O0MI.w.uNaQvxFOFVz9rYe7DWOrTItqCNRSBfCIRkLyKaOHjo5rO2', 1, NOW()),
(4, 'DENTISTA', 'Dra. Juliana Mendes', 'juliana.endo@smilehub.com', '$2a$10$O0MI.w.uNaQvxFOFVz9rYe7DWOrTItqCNRSBfCIRkLyKaOHjo5rO2', 1, NOW()),

-- Pacientes (ids 5 a 12)
(5, 'PACIENTE', 'Maria Oliveira', 'maria.oliveira@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(6, 'PACIENTE', 'João Santos', 'joao.santos@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(7, 'PACIENTE', 'Pedro Henrique Alencar', 'pedro.alencar@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(8, 'PACIENTE', 'Carla Diaz', 'carla.diaz@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(9, 'PACIENTE', 'Lucas Souza Gabriel', 'lucas.souza@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(10, 'PACIENTE', 'Beatriz Rodrigues', 'bia.rodrigues@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(11, 'PACIENTE', 'Marcos Vinícius Costa', 'marcos.costa@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW()),
(12, 'PACIENTE', 'Fernanda Lima Castro', 'fernanda.lima@email.com', '$2a$10$aHwvd03RWOmlO/QsGlvC9eCsJseVt3PTzTkv1tdV7o77ViQlMJiB2', 1, NOW());

-- Tabela Filha: Administradores
INSERT INTO administradores (id) VALUES (1);

-- Tabela Filha: Dentistas
INSERT INTO dentistas (id, cpf, cro) VALUES
(2, '11111111111', 'CRO-SP 12345'),
(3, '44455566622', 'CRO-SP 98765'),
(4, '77788899933', 'CRO-SP 45612');

-- Tabela Filha: Pacientes
INSERT INTO pacientes (id, cpf, telefone) VALUES
(5, '22222222222', '(11) 98888-0001'),
(6, '33333333333', '(11) 98888-0002'),
(7, '44444444444', '(11) 97777-1111'),
(8, '55555555555', '(11) 96666-2222'),
(9, '66666666666', '(21) 95555-3333'),
(10, '77777777777', '(31) 94444-4444'),
(11, '88888888888', '(19) 93333-5555'),
(12, '99999999999', '(11) 92222-6666');


-- =============================================================================
-- 2. ESPECIALIDADES E VÍNCULO COM DENTISTAS
-- =============================================================================
INSERT INTO especialidade (id, nome, usuario_id, ativo, data_criacao) VALUES
(1, 'Ortodontia', 1, 1, NOW()),
(2, 'Endodontia', 1, 1, NOW()),
(3, 'Clínico Geral', 1, 1, NOW()),
(4, 'Odontopediatria', 1, 1, NOW()),
(5, 'Periodontia', 1, 1, NOW()),
(6, 'Prótese Dentária', 1, 1, NOW());

-- Vínculos Dentistas X Especialidades
INSERT INTO dentista_especialidade (id, id_dentista, id_especialidade, data_cadastro) VALUES
(1, 2, 1, NOW()), -- Dra. Ana: Ortodontia
(2, 2, 3, NOW()), -- Dra. Ana: Clínico Geral
(3, 3, 1, NOW()), -- Dr. Carlos: Ortodontia
(4, 4, 2, NOW()), -- Dra. Juliana: Endodontia
(5, 4, 5, NOW()); -- Dra. Juliana: Periodontia


-- =============================================================================
-- 3. SERVIÇOS ODONTOLÓGICOS (Catálogo de Preços)
-- =============================================================================
INSERT INTO servico (id, nome, valor, ativo, usuario_id) VALUES
(1, 'Consulta de avaliação', 150.00, 1, 1),
(2, 'Limpeza dental (Profilaxia)', 220.00, 1, 1),
(3, 'Restauração em resina', 350.00, 1, 1),
(4, 'Tratamento de Canal (Endodontia) Monorradicular', 680.00, 1, 1),
(5, 'Tratamento de Canal (Endodontia) Multirradicular', 950.00, 1, 1),
(6, 'Manutenção de Aparelho Ortodôntico', 180.00, 1, 1),
(7, 'Instalação de Aparelho Fixo Metálico', 800.00, 1, 1),
(8, 'Extração Dentária Simples', 250.00, 1, 1),
(9, 'Clareamento Dental Caseiro (Kit)', 600.00, 1, 1),
(10, 'Raspagem Periodontal (por sessão)', 300.00, 1, 1);


-- =============================================================================
-- 3.1 MATERIAIS E ESTOQUE
-- =============================================================================
INSERT INTO material (id, nome, quantidade, quantidade_inicial, ativo, alerta_estoque_baixo, usuario_id) VALUES
(1, 'Luvas descartáveis (par)', 500, 500, 1, 0, 1),
(2, 'Resina composta (seringa)', 80, 80, 1, 0, 1),
(3, 'Anestésico local (tubete)', 120, 120, 1, 0, 1),
(4, 'Broca diamantada', 200, 200, 1, 0, 1),
(5, 'Algodão (pacote)', 60, 60, 1, 0, 1);

INSERT INTO servico_material (id, id_servico, id_material, quantidade) VALUES
(1, 1, 1, 1),
(2, 1, 5, 1),
(3, 2, 1, 1),
(4, 2, 4, 2),
(5, 2, 5, 2),
(6, 3, 1, 1),
(7, 3, 2, 1),
(8, 3, 3, 1),
(9, 3, 4, 1),
(10, 4, 1, 1),
(11, 4, 3, 2),
(12, 4, 4, 2),
(13, 8, 1, 1),
(14, 8, 3, 2),
(15, 8, 5, 1);


-- =============================================================================
-- 4. MOTIVOS DE CANCELAMENTO
-- =============================================================================
INSERT INTO motivo_cancelamento (id, descricao, usuario_criacao, data_criacao) VALUES
(1, 'Paciente não compareceu (No-show)', 1, NOW()),
(2, 'Reagendamento solicitado pelo paciente', 1, NOW()),
(3, 'Imprevisto operacional/clínico do dentista', 1, NOW()),
(4, 'Problemas de saúde/força maior informado', 1, NOW());


-- =============================================================================
-- 5. CONSULTAS (Histórico Passado e Agendamentos Futuros)
-- Mês de simulação baseado em Junho de 2026
-- =============================================================================
INSERT INTO consulta (id, id_paciente, id_dentista, id_usuario, descricao, data_inicio, data_fim, data_registro, status) VALUES
-- Consultas Finalizadas (Histórico)
(1, 5, 2, 2, 'Limpeza e revisão do odontograma', '2026-06-05 14:00:00', '2026-06-05 14:45:00', NOW(), 'FINALIZADA'),
(2, 6, 4, 4, 'Tratamento de urgência - dor de dente molares', '2026-06-06 09:00:00', '2026-06-06 10:00:00', NOW(), 'FINALIZADA'),
(3, 7, 3, 3, 'Colocação de aparelho ortodôntico superior', '2026-06-08 11:00:00', '2026-06-08 12:30:00', NOW(), 'FINALIZADA'),
(4, 8, 2, 1, 'Avaliação geral inicial estética', '2026-06-10 16:00:00', '2026-06-10 16:30:00', NOW(), 'FINALIZADA'),
(5, 9, 4, 4, 'Primeira sessão de tratamento periodontal', '2026-06-12 15:00:00', '2026-06-12 16:00:00', NOW(), 'FINALIZADA'),

-- Consultas Canceladas
(6, 10, 2, 1, 'Consulta de avaliação com a Dra. Ana', '2026-06-11 10:00:00', '2026-06-11 10:30:00', NOW(), 'CANCELADA'),

-- Consultas Agendadas (Futuro próximo)
(7, 5, 2, 2, 'Avaliação ortodôntica inicial pós-limpeza', '2026-06-20 09:00:00', '2026-06-20 09:30:00', NOW(), 'AGENDADA'),
(8, 6, 2, 1, 'Primeira consulta - clínico geral acompanhamento', '2026-06-22 10:00:00', '2026-06-22 10:30:00', NOW(), 'AGENDADA'),
(9, 7, 3, 3, 'Manutenção mensal do aparelho fixo', '2026-07-08 14:00:00', '2026-07-08 14:30:00', NOW(), 'AGENDADA'),
(10, 11, 4, 4, 'Tratamento de canal dente 46', '2026-06-18 14:00:00', '2026-06-18 15:30:00', NOW(), 'AGENDADA'),
(11, 12, 2, 2, 'Clareamento dental acompanhamento moldeira', '2026-06-19 16:00:00', '2026-06-19 16:45:00', NOW(), 'AGENDADA');


-- =============================================================================
-- 6. CONSULTA SERVIÇO (Vínculo Financeiro de Serviços Prestados nas Consultas)
-- =============================================================================
INSERT INTO consulta_servico (id, id_consulta, id_servico, valor) VALUES
-- Serviços da consulta 1 (Maria - Limpeza)
(1, 1, 2, 220.00),
-- Serviços da consulta 2 (João - Canal Multirradicular + Avaliação de urgência)
(2, 2, 5, 950.00),
(3, 2, 1, 150.00),
-- Serviços da consulta 3 (Pedro - Aparelho)
(4, 3, 7, 800.00),
-- Serviços da consulta 4 (Carla - Avaliação)
(5, 4, 1, 150.00),
-- Serviços da consulta 5 (Lucas - Raspagem)
(6, 5, 10, 300.00),
-- Serviços previstos para as agendadas (Exemplos)
(7, 7, 1, 150.00),
(8, 8, 1, 150.00),
(9, 9, 6, 180.00),
(10, 10, 5, 950.00),
(11, 11, 9, 600.00);


-- =============================================================================
-- 7. ODONTOGRAMAS E SITUAÇÃO DOS DENTES (Prontuários Visuais)
-- =============================================================================
-- Prontuário 1: Maria Oliveira (id_paciente 5)
INSERT INTO odontograma (id, id_paciente, id_dentista, data_ultima_alteracao, observacoes) VALUES
(1, 5, 2, NOW(), 'Paciente com histórico de cárie em molares. Boa higiene recente.');

INSERT INTO dente (id, id_odontograma, numero, status, observacoes) VALUES
(1, 1, 16, 'cariado', 'Cárie oclusal superficial'),
(2, 1, 26, 'restaurado', 'Restauração em resina realizada anteriormente'),
(3, 1, 36, 'tratamento', 'Tratamento de canal em andamento na raiz distal'),
(4, 1, 11, 'higido', 'Sem alterações clínicas');

-- Prontuário 2: João Santos (id_paciente 6)
INSERT INTO odontograma (id, id_paciente, id_dentista, data_ultima_alteracao, observacoes) VALUES
(2, 6, 4, NOW(), 'Caso complexo de endodontia múltipla necessária por negligência de higiene.');

INSERT INTO dente (id, id_odontograma, numero, status, observacoes) VALUES
(5, 2, 14, 'cariado', 'Cárie extensa infiltrando a polpa'),
(6, 2, 15, 'cariado', 'Cárie interproximal detectada em raio-X'),
(7, 2, 46, 'ausente', 'Extraído em outra clínica em 2022'),
(8, 2, 21, 'restaurado', 'Restauração estética de fratura antiga');

-- Prontuário 3: Pedro Henrique (id_paciente 7)
INSERT INTO odontograma (id, id_paciente, id_dentista, data_ultima_alteracao, observacoes) VALUES
(3, 7, 3, NOW(), 'Paciente ortodôntico. Dentes hígidos no geral, com apinhamento severo inferior.');

INSERT INTO dente (id, id_odontograma, numero, status, observacoes) VALUES
(9, 3, 11, 'higido', 'Aparelho fixo instalado'),
(10, 3, 21, 'higido', 'Aparelho fixo instalado'),
(11, 3, 31, 'higido', 'Apinhamento acentuado'),
(12, 3, 41, 'higido', 'Apinhamento acentuado');


-- =============================================================================
-- 8. PRESCRIÇÕES E MEDICAMENTOS (Receituário Clínico das Finalizadas)
-- =============================================================================
-- Prescrição para Consulta 1 (Maria)
INSERT INTO prescricao (id, id_consulta, observacoes, data_emissao) VALUES
(1, 1, 'Evitar alimentos com corantes fortes (café, refrigerante, açaí) nas próximas 48h devido à profilaxia severa.', NOW());

INSERT INTO medicamento (id, id_prescricao, nome, dosagem, frequencia, duracao) VALUES
(1, 1, 'Clorexidina bucal 0,12%', '10ml para bochecho', 'De 12 em 12 horas após escovação', 'Por 7 dias');

-- Prescrição para Consulta 2 (João - Pós Urgência/Canal)
INSERT INTO prescricao (id, id_consulta, observacoes, data_emissao) VALUES
(2, 2, 'Repouso de atividades físicas hoje. Se houver inchaço acentuado, aplicar compressa fria por 15 min.', NOW());

INSERT INTO medicamento (id, id_prescricao, nome, dosagem, frequencia, duracao) VALUES
(2, 2, 'Ibuprofeno', '600mg', 'De 8 em 8 horas se houver dor/inflamação', 'Por 3 dias'),
(3, 2, 'Dipirona Sódica', '500mg', 'De 6 em 6 horas em caso de dor persistente', 'Por 2 dias'),
(4, 2, 'Amoxicilina', '500mg', 'Tomar de 8 em 8 horas rigorosamente', 'Por 7 dias');


-- =============================================================================
-- 9. AJUSTE DOS AUTO-INCREMENTOS (Evita quebras nas inserções via API da aplicação)
-- Configurando o ponteiro para começar em 100 para todos os novos registros
-- =============================================================================
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