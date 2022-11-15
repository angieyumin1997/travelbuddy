use travelbuddy;

insert into locals (location,activities,description,username)
    values('Singapore','advice','Welcome to singapore! I am provide some tips on travelling around this green city!','fred');

insert into locals (location,activities,description,username)
    values('Singapore','coffee/drinks','Welcome to Germany!','mary');

insert into trips (start,end,location,description,type,username)
    values('2022-12-12','2022-12-22','Germany','I am going to germany!','Adventure Travel','fred');

insert into trips (start,end,location,description,type,username)
    values('2022-12-12','2022-12-22','France','I am going to france!','Road Trip','mary');
