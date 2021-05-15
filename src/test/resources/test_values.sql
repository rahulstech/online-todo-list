-- inserting todolists
INSERT INTO todolists (id,created,title) VALUES ("1","2021-05-14 15:03","Todos For May 14, 2021");
INSERT INTO todolists (id,created,title) VALUES ("2","2021-05-16 17:00", "Todos For May 16, 2021");
INSERT INTO todolists (id,created,title) VALUES ("3","2021-05-10 20:26", "Todos For May 10, 2021");

-- inserting todos for list [1]
INSERT INTO todos (id,content,complete,listId) VALUES ("1","List 1 Todo 1",0,"1");
INSERT INTO todos (id,content,complete,listId) VALUES ("2","List 1 Todo 2",1,"1");
INSERT INTO todos (id,content,complete,listId) VALUES ("3","List 1 Todo 3",0,"1");
INSERT INTO todos (id,content,complete,listId) VALUES ("4","List 1 Todo 4",0,"1");

-- inserting todos for list [2]
INSERT INTO todos (id,content,complete,listId) VALUES ("5","List 2 Todo 1",1,"2");
INSERT INTO todos (id,content,complete,listId) VALUES ("6","List 2 Todo 2",0,"2");
INSERT INTO todos (id,content,complete,listId) VALUES ("7","List 2 Todo 3",1,"2");