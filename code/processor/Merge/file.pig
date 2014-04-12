L = load 'longWithScores1t.txt' USING PigStorage('~') as (handlel,summary,scorel);
E = load 'External_Link_Score_final.txt' USING PigStorage('~') as (handlee,scoree);
S = load 'short_abstarct_score_final.txt' USING PigStorage('~') as (handles,scores);
I = load 'infoBoxScoredData.txt' USING PigStorage('~') as (handlei,ib,scorei);
JLE = join L by handlel, E by handlee;
PLE = foreach JLE generate (handlel,summary,scorel,scoree); 
LE = foreach PLE generate flatten($0);

JLES = join LE by handlel, S by handles;
PLES = foreach JLES generate (handlel,summary,scorel,scoree,scores);
LES = foreach PLES generate flatten($0);

JLESI = join LES by handlel, I by handlei;
PLESI = foreach JLESI generate (handlel,summary,I::ib,scorel,scoree,scores,scorei);
LESI = foreach PLESI generate flatten($0);

store LESI into 'test3' using PigStorage('~');