SET default_storage_engine= Innodb;
create database Loghme;
use Loghme;
create table Users(
	id int,
    firstName varchar(30) not null,
    lastName varchar(40) not null,
    phoneNumber varchar(11) not null,
    email varchar(50) not null,
    password varchar(50) not null,
    x int not null,
    y int not null,
    credit int,
    primary key(id, email)
);
create table ShoppingCarts(
	userId int,
    isEmpty boolean,
    restaurantId varchar(60),
    restaurantName varchar(100),
    totalPayment int,
    isFoodParty int,
    firstPartyFoodEnteredTime timestamp,
    primary key(userId),
    foreign key(userId) references Users(id) on delete cascade on update cascade
);
create table CartItems(
	userId int,
    foodName varchar(100),
    price int not null,
    number int not null,
    isPartyFood boolean,
    primary key(userId, foodName),
    foreign key(userId) references ShoppingCarts(userId) on delete cascade on update cascade
);
create table Orders(
	userId int,
    id int,
    restaurantId varchar(60) not null,
    restaurantName varchar(100) not null,
    totalPayment int not null,
    state varchar(10) not null,
    finalizationTime timestamp,
    deliveringTime time,
    deliveryId varchar(60),
	primary key(userId, id),
    foreign key(userId) references Users(id) on delete cascade on update cascade
);
create table OrderItems(
	userId int,
	orderId int,
    foodName varchar(100),
    price int not null,
    number int not null,
    primary key(userId, orderId, foodName),
    foreign key(userId, orderId) references Orders(userId, id) on delete cascade on update cascade
);
create table Restaurants(
	id varchar(60),
    name varchar(100) not null,
    x int not null,
    y int not null,
	logo varchar(250),
    primary key(id)
);
create table Foods(
	restaurantId varchar(60),
    name varchar(100),
    price int not null,
    description varchar(250),
    image varchar(250),
    popularity float,
    primary key(restaurantId, name),
    foreign key(restaurantId) references Restaurants(id) on delete cascade on update cascade
);
create table Foodparty(
	enteredDate timestamp,
    primary key(enteredDate)
);
create table PartyFoods(
	restaurantId varchar(60),
    name varchar(100),
    restaurantName varchar(100) not null,
	oldPrice int not null,
    price int not null,
    count int not null,
    description varchar(250),
    image varchar(250),
    popularity float,
    primary key(restaurantId, name),
    foreign key(restaurantId) references Restaurants(id) on delete cascade on update cascade
);

insert into Users(id, firstName, lastName, phoneNumber, email, password, x, y, credit)
values (0, "Ehsan", "Khames Panah", "09123456789", "ekhamespanah@yahoo.com","123456", 0, 0, 100000);

insert into ShoppingCarts(userId, isEmpty, isFoodParty, totalPayment)
values (0, true, 0, 0);