package codes.bjornnorgren.testgrades.dto;

public class Student {
    public int rollNo;
    public String name;
    public int score;
    public String grade;

    public Student() {
    }

    public Student(int rollNo, String name, int score, String grade) {
        this.rollNo = rollNo;
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNo=" + rollNo +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", grade='" + grade + '\'' +
                '}';
    }
}
