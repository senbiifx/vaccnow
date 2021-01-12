CREATE TABLE branch(
    branch_id int NOT NULL IDENTITY,
    description varchar(50),
    branch_code varchar(20) PRIMARY KEY
);

CREATE TABLE vaccine(
    vaccine_id int NOT NULL IDENTITY,
    description varchar(50),
    vaccine_code varchar(20) PRIMARY KEY
);

CREATE TABLE branch_vaccine(
    branch_vaccine_id int NOT NULL IDENTITY PRIMARY KEY,
    branch_code varchar(20) not null,
    vaccine_code varchar(20) not null,
    CONSTRAINT FK_Branch FOREIGN KEY (branch_code) REFERENCES branch(branch_code),
    CONSTRAINT FK_Vaccine FOREIGN KEY (vaccine_code) REFERENCES vaccine(vaccine_code),
);

CREATE TABLE payment_method(
    payment_method_id int NOT NULL IDENTITY PRIMARY KEY,
    description varchar(50)
);


CREATE TABLE time_slot(
    time_slot_id int NOT NULL IDENTITY PRIMARY KEY,
    from_time varchar(20),
    to_time varchar(20)
);

CREATE TABLE customer(
    customer_id int NOT NULL IDENTITY PRIMARY KEY,
    customer_name varchar(50),
    customer_national_number varchar(20),
    email varchar(50),
);

CREATE TABLE vaccination_schedule(
    vaccination_schedule_id int NOT NULL IDENTITY PRIMARY KEY,
    vaccination_schedule_code varchar(50) NOT NULL UNIQUE,
    schedule_date DATE NOT NULL,
    time_slot_id int NOT NULL,
    branch_code varchar(20) NOT NULL,
    vaccine_code varchar(20) NOT NULL,
    customer_id int NOT NULL,
    payment_method_id int NOT NULL,
    confirmed bit not null default 0,
    applied bit not null default 0,
    CONSTRAINT FK_Schedule_TimeSlot FOREIGN KEY (time_slot_id) REFERENCES time_slot(time_slot_id),
    CONSTRAINT FK_Schedule_BranchCode FOREIGN KEY (branch_code) REFERENCES branch(branch_code),
    CONSTRAINT FK_Schedule_VaccineCode FOREIGN KEY (vaccine_code) REFERENCES vaccine(vaccine_code),
    CONSTRAINT FK_Schedule_CustomerId FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT FK_Schedule_PaymentMethod FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id)
);