CREATE TABLE remote_Web_Log
(   LogId int IDENTITY PRIMARY KEY,
    ResStatus int,
    Controller varchar(50),
    Method varchar(100),
    Params varchar(max),
    UserPhone varchar(10),
    PrgKind varchar(10),
    EX varchar(max),
	EDPDatetime datetime DEFAULT GETDATE()
);