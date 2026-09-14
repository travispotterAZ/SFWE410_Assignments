-- Sample People
INSERT INTO person (id, name, major, dept, date_of_birth, phone, email) VALUES
    (1, 'Alice Johnson', 'Computer Science',  'Computer Science',  '2001-04-12', '520-555-0101', 'alice.johnson@example.edu'),
    (2, 'Brian Lee',      'Kinesiology',       'Sports Science',    '2000-09-23', '520-555-0102', 'brian.lee@example.edu'),
    (3, 'Carla Nguyen',   'Studio Art',        'Fine Arts',         '2002-01-30', '520-555-0103', 'carla.nguyen@example.edu'),
    (4, 'David Kim',      'English',           'Literature',        '1999-11-08', '520-555-0104', 'david.kim@example.edu'),
    (5, 'Elena Ruiz',     'Business',          'Management',        '2001-07-19', '520-555-0105', 'elena.ruiz@example.edu'),
    (6, 'Frank Osei',     'Computer Science',  'Computer Science',  '2000-03-05', '520-555-0106', 'frank.osei@example.edu');

-- Sample Organizations, each with a president (a Person)
INSERT INTO organization (id, name, category, established_date, president_id) VALUES
    (1, 'Wildcat Soccer Club',  'SPORTS',     '2015-03-01', 2),
    (2, 'Campus Fitness Crew',  'FITNESS',    '2018-09-15', 5),
    (3, 'Visual Arts Society',  'ARTS',       '2012-01-20', 3),
    (4, 'Literary Circle',      'LITERATURE', '2010-11-05', 4);

-- "Member of" associations (many-to-many, every organization has >= 1 member)
INSERT INTO person_organization (person_id, organization_id) VALUES
    (1, 1), -- Alice: Wildcat Soccer Club
    (1, 3), -- Alice: Visual Arts Society
    (2, 2), -- Brian: Campus Fitness Crew
    (3, 4), -- Carla: Literary Circle
    (4, 3), -- David: Visual Arts Society
    (5, 1), -- Elena: Wildcat Soccer Club
    (5, 2), -- Elena: Campus Fitness Crew
    (6, 4), -- Frank: Literary Circle
    (6, 2); -- Frank: Campus Fitness Crew
