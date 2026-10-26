CREATE TABLE tb_customer_account_opening (
                                             id VARCHAR(36) NOT NULL,
                                             name VARCHAR(255) NOT NULL,
                                             cpf VARCHAR(11) NOT NULL,
                                             birth_date VARCHAR(255) NOT NULL,
                                             email VARCHAR(255) NOT NULL,
                                             status VARCHAR(50),
                                             created_at TIMESTAMP,

                                             CONSTRAINT pk_customer_account_opening
                                                 PRIMARY KEY (id),

                                             CONSTRAINT uk_customer_account_opening_cpf
                                                 UNIQUE (cpf)
        INSERT INTO tb_customer_account_opening (
        id,
        name,
        cpf,
        birth_date,
        email,
        status,
        created_at
        )
        VALUES
        (
        gen_random_uuid():: VARCHAR,
        'João Silva',
        '12345678901',
        '1995-05-15',
        'joao.silva@email.com',
        'ACTIVE',
        CURRENT_TIMESTAMP
        ), (
    gen_random_uuid
(
):: VARCHAR,
    'Maria Santos',
    '98765432100',
    '1990-08-20',
    'maria.santos@email.com',
    'ACTIVE',
    CURRENT_TIMESTAMP
    ), (
    gen_random_uuid()::VARCHAR,
    'Pedro Oliveira',
    '45678912300',
    '1998-02-10',
    'pedro.oliveira@email.com',
    'ACTIVE',
    CURRENT_TIMESTAMP
), (
    gen_random_uuid()::VARCHAR,
    'Ana Costa',
    '78912345600',
    '2000-11-25',
    'ana.costa@email.com',
    'ACTIVE',
    CURRENT_TIMESTAMP
);
);
