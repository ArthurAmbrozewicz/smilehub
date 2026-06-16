# SmileHub

Sistema web de **gestão de consultas odontológicas**, desenvolvido como Projeto Final do programa **Wise Start**. A aplicação permite agendar, acompanhar e finalizar atendimentos, com controle de acesso por perfil, ficha odontológica digital e indicadores gerenciais.

---

## Sobre o projeto

O sistema cobre o fluxo completo de uma clínica:

- cadastro de pacientes, dentistas e especialidades;
- agendamento e gestão de consultas;
- atendimento clínico com odontograma e prescrição;
- dashboard com métricas e gráficos;
- notificações internas.

---

## Funcionalidades

### Autenticação e perfis de acesso

| Perfil | Permissões principais |
|--------|------------------------|
| **Administrador** | Acesso total: usuários, cadastros, consultas, dashboard, serviços e motivos de cancelamento |
| **Dentista** | Agenda, consultas próprias, cadastro de pacientes, ficha odontológica e prescrições |
| **Paciente** | Cadastro no sistema (sem acesso à área logada) |

- Login com **JWT** (token armazenado no navegador)
- Rotas protegidas no front-end e no back-end (Spring Security)

### Cadastros

- **Pacientes** — nome, CPF, e-mail, telefone
- **Dentistas** — CPF, CRO, especialidades vinculadas, foto de perfil
- **Especialidades** — cadastro e vínculo N:N com dentistas
- **Serviços odontológicos** — catálogo de procedimentos com valores
- **Administradores** — gestão exclusiva do perfil admin
- **Motivos de cancelamento** — obrigatórios ao cancelar uma consulta

### Consultas

- Criar, editar, visualizar, cancelar e finalizar consultas
- Vínculo com paciente, dentista, serviços e horários
- Status: `AGENDADA`, `CANCELADA`, `FINALIZADA`
- Validações de negócio:
  - sem conflito de horário para o mesmo dentista;
  - não permite agendamento em datas passadas;
  - horário final posterior ao inicial;
  - motivo obrigatório no cancelamento

### Agenda (calendário)

- Visualização por **mês**, **semana** e **dia**
- Filtros por dentista, paciente e status
- Ação **Realizar consulta** — abre a ficha odontológica do paciente

### Ficha odontológica

- **Odontograma** interativo (32 dentes, numeração FDI)
- Status por dente: hígido, cariado, restaurado, extraído, prótese, em tratamento, observação
- **Prescrição médica** com medicamentos (opcional)
- Geração de **PDF** da prescrição
- Persistência no banco (`odontograma`, `dente`, `prescricao`, `medicamento`)
- Modo somente leitura na listagem de pacientes

### Dashboard

- Indicadores: consultas totais, % cancelamento, receita realizada e a receber
- Filtro por período (data inicial e final)
- Gráficos com **Chart.js**:
  - barras — consultas por dentista;
  - rosca — status das consultas;
  - linhas — evolução diária;
  - barras horizontais — valor por dentista;
  - polar — consultas finalizadas por dentista

### Notificações

- Alertas internos para eventos do sistema (ex.: nova consulta agendada)
- Painel na navbar com contador de não lidas

### Extras implementados (além do escopo mínimo)

- Docker + Nginx para deploy em produção
- Script SQL de seed (`data.sql`) com dados de exemplo
- Máscaras e validações de CPF, telefone e horário no front-end
- Layout responsivo (sidebar, calendário e listagens)
- Upload de foto do dentista

---

## Tecnologias utilizadas

### Front-end

| Tecnologia | Uso |
|------------|-----|
| **Angular 19** | Framework SPA, componentes standalone |
| **TypeScript** | Linguagem principal |
| **Angular Material** | Componentes base e temas |
| **RxJS** | Programação reativa e HTTP |
| **Chart.js** | Gráficos do dashboard |
| **jsPDF** | Geração de PDF da prescrição |
| **Tailwind CSS 4** | Utilitários de estilo |
| **SCSS** | Estilos globais e por feature |

### Back-end

| Tecnologia | Uso |
|------------|-----|
| **Java 17** | Linguagem |
| **Spring Boot 3.5** | API REST |
| **Spring Data JPA** | Persistência |
| **Spring Security + JWT** | Autenticação e autorização |
| **Hibernate** | ORM (herança JOINED para usuários) |
| **MySQL** | Banco relacional (dev) |
| **BCrypt** | Hash de senhas |
| **Maven** | Build e dependências |

### Infraestrutura

| Tecnologia | Uso |
|------------|-----|
| **Docker** | Containerização front e back |
| **Nginx** | Servidor estático do Angular |
| **Docker Compose / Traefik** | Orquestração em produção |

---

## Arquitetura

```
┌─────────────────┐     HTTP/REST + JWT     ┌─────────────────┐
│  Angular (SPA)  │ ◄──────────────────────► │  Spring Boot    │
│  porta 8090     │                          │  porta 8080     │
└─────────────────┘                          └────────┬────────┘
                                                      │
                                                      ▼
                                             ┌─────────────────┐
                                             │  MySQL / Postgres│
                                             │  banco: smilehub │
                                             └─────────────────┘
```

- **Context path da API:** `/smilehub`
- **Base da API:** `http://localhost:8080/smilehub/api`

---

## Estrutura do repositório

```
ProejtoSmileHub/
├── README.md                          ← este arquivo
├── smilehub/                          ← Back-end (Spring Boot)
│   ├── src/main/java/com/smilehub/smilehub/
│   │   ├── controllers/               ← Endpoints REST
│   │   ├── services/                  ← Regras de negócio
│   │   ├── entities/                  ← Entidades JPA
│   │   ├── repositories/              ← Acesso a dados
│   │   ├── config/                    ← Security, CORS, seed
│   │   └── security/                  ← JWT Filter e Service
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── data.sql                   ← Dados iniciais (dev)
│   ├── docker-compose.yml             ← Deploy produção
│   └── pom.xml
│
└── smilehub front/
    └── smilehub-frontend/             ← Front-end (Angular)
        ├── src/app/
        │   ├── auth/                  ← Login
        │   ├── pages/                 ← CRUDs e dashboard
        │   ├── features/              ← Calendário, ficha odontológica
        │   ├── core/                  ← Services, models, interceptors
        │   ├── shared/                ← Sidebar, modais, listagem
        │   └── layout/                ← Navbar
        ├── public/                    ← Logo e favicon
        ├── angular.json
        └── package.json
```

---

## Pré-requisitos

- **Node.js** 20+ e **npm**
- **Java 17** e **Maven** (ou `./mvnw` incluso no projeto)
- **MySQL** 8+ (desenvolvimento local)
- **Git**

---

## Como executar localmente

### 1. Banco de dados

Crie o banco MySQL:

```sql
CREATE DATABASE smilehub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Configure as credenciais em `smilehub/src/main/resources/application.properties` (usuário, senha e URL).

> Na primeira execução, o Hibernate cria/atualiza as tabelas e o arquivo `data.sql` popula dados de exemplo.

### 2. Back-end

```bash
cd smilehub
./mvnw spring-boot:run
```

API disponível em: `http://localhost:8080/smilehub/api`

### 3. Front-end

```bash
cd "smilehub front/smilehub-frontend"
npm install
npm start
```

Aplicação disponível em: `http://localhost:8090`

### Credenciais de teste (seed)

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Administrador | `admin@smilehub.com` | `admin123` |
| Dentista | `dentista@smilehub.com` | `dentista123` |

Outros dentistas de exemplo: `carlos.orto@smilehub.com` / `carlos123`, `juliana.endo@smilehub.com` / `juliana123`.

---

## Principais endpoints da API

| Recurso | Base path | Descrição |
|---------|-----------|-----------|
| Autenticação | `POST /api/auth/login` | Login (público) |
| Consultas | `/api/consultas` | CRUD, cancelar, finalizar |
| Pacientes | `/api/pacientes` | CRUD de pacientes |
| Dentistas | `/api/dentistas` | CRUD de dentistas |
| Especialidades | `/api/especialidades` | CRUD de especialidades |
| Serviços | `/api/servicos` | Catálogo de serviços |
| Administradores | `/api/administradores` | Gestão de admins |
| Motivos cancelamento | `/api/motivos-cancelamento` | Motivos de cancelamento |
| Odontogramas | `/api/odontogramas` | Ficha odontológica |
| Prescrições | `/api/prescricoes` | Prescrições e histórico |
| Notificações | `/api/notificacoes` | Alertas do usuário |
| Usuários | `/api/usuarios` | Perfil e foto |

> Todas as rotas (exceto login e foto pública) exigem header `Authorization: Bearer <token>`.

---

## Modelo de dados (principais tabelas)

Conforme especificação acadêmica, com extensões do SmileHub:

| Tabela | Descrição |
|--------|-----------|
| `usuarios` | Base de usuários (herança JOINED) |
| `administradores`, `dentistas`, `pacientes` | Subtipos de usuário |
| `especialidade` | Especialidades odontológicas |
| `dentista_especialidade` | Vínculo N:N |
| `servico` | Procedimentos e valores |
| `consulta` | Agendamentos |
| `consulta_servico` | Serviços por consulta |
| `motivo_cancelamento` | Motivos de cancelamento |
| `odontograma`, `dente` | Ficha odontológica |
| `prescricao`, `medicamento` | Receituário |
| `notificacao` | Notificações internas |

---

## Deploy (Docker)

O projeto inclui `Dockerfile` no front-end e imagens publicadas para produção. O `docker-compose.yml` do backend configura:

- **Front-end:** `https://av3.com.br/smilehub`
- **API:** `https://av3.com.br/smilehub/api`

Build local do front-end:

```bash
cd "smilehub front/smilehub-frontend"
npm run build
```

---

## Regras de negócio atendidas

Conforme o documento do Projeto Final:

1. Não permite conflito de horário para o mesmo dentista
2. Não permite agendamento em datas passadas
3. Cancelamento exige motivo cadastrado
4. Dentista ↔ especialidade (N:N)
5. Apenas administrador gerencia usuários administrativos
6. Dentista visualiza suas próprias consultas
7. Administrador visualiza todas as consultas
8. Horário final deve ser posterior ao inicial

---

## Identidade visual

- **Nome:** SmileHub
- **Paleta:** azul turquesa `#03bfc1`, tons neutros e acentos semânticos (verde, vermelho, azul)
- **Logo:** `public/logo-smilehub.png`

---

## Contexto acadêmico

| Item | Informação |
|------|------------|
| Programa | Wise Start |
| Projeto | Sistema de Gestão de Consultas Odontológicas |
| Stack exigida | Angular + Spring Boot + Banco SQL |
| Prazo de entrega | 16/06/2026 |
| Apresentação | 18/06/2026 |

---

## Licença

Projeto acadêmico desenvolvido para fins educacionais no programa Wise Start.
