import java.util.*;

// ============================
// Date Entity
// ============================
class DateInfo {
  private String date;
  public DateInfo(String date) { this.date = date; }
  public String getDate() { return date; }
}

// ============================
// RoomClass and Subclasses
// ============================
abstract class RoomClass {
  protected int price;
  protected int vacancy;
  public RoomClass(String price, String vacancy) {
    this.price = Integer.parseInt(price);
    this.vacancy = Integer.parseInt(vacancy);
  }
  public int getVacancy() { return vacancy; }
  public void decrementVacancy() { if (vacancy > 0) vacancy--; }
  public void incrementVacancy() { vacancy++; }
  public int getPrice() { return price; }
  public abstract String getType();
}
class SingleRoom extends RoomClass {
  public SingleRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Single"; }
}
class DoubleRoom extends RoomClass {
  public DoubleRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Double"; }
}
class SuiteRoom extends RoomClass {
  public SuiteRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Suite"; }
}

// ============================
// Room Entity
// ============================
class Room {
  private int roomNumber;
  private DateInfo checkinDate;
  private DateInfo checkoutDate;
  private List<RoomClass> roomClasses;
  public Room(int roomNumber, String checkinDate, String checkoutDate, List<RoomClass> roomClasses) {
    this.roomNumber = roomNumber;
    this.checkinDate = new DateInfo(checkinDate);
    this.checkoutDate = new DateInfo(checkoutDate);
    this.roomClasses = roomClasses;
  }
  public List<RoomClass> getRoomClasses() { return roomClasses; }
  public DateInfo getCheckinDate() { return checkinDate; }
  public DateInfo getCheckoutDate() { return checkoutDate; }
  public int getRoomNumber() { return roomNumber; }
}

// ============================
// Reservation Entity
// ============================
class Reservation {
  private static int counter = 1000;
  private final int reservationId;
  private int roomNumber;
  private String roomType;
  private String checkinDate;
  private String checkoutDate;
  private boolean checkedOut;
  private boolean checkedIn;

  public Reservation(int roomNumber, String roomType, String checkinDate, String checkoutDate) {
    this.reservationId = counter++;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.checkinDate = checkinDate;
    this.checkoutDate = checkoutDate;
    this.checkedOut = false;
    this.checkedIn = false;
  }

  public int getReservationId() { return reservationId; }
  public int getRoomNumber() { return roomNumber; }
  public String getRoomType() { return roomType; }
  public String getCheckinDate() { return checkinDate; }
  public String getCheckoutDate() { return checkoutDate; }
  public boolean isCheckedOut() { return checkedOut; }
  public boolean isCheckedIn() { return checkedIn; }
  public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }
  public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

  public boolean matches(String roomType, String checkinDate, String checkoutDate, int roomNumber) {
    return this.roomType.equalsIgnoreCase(roomType) &&
           this.checkinDate.equals(checkinDate) &&
           this.checkoutDate.equals(checkoutDate) &&
           this.roomNumber == roomNumber;
  }
}

// ============================
// ReservationControl
// ============================
class ReservationControl {
  private List<Room> vacantRooms;
  private Map<Integer, Reservation> reservations;

  public ReservationControl() {
    this.vacantRooms = new ArrayList<>();
    this.reservations = new HashMap<>();
  }

  public List<Room> findVacantRoom(String checkinDate, String checkoutDate, int price, String type) {
    List<Room> matchedRooms = new ArrayList<>();
    for (Room room : vacantRooms) {
      for (RoomClass rc : room.getRoomClasses()) {
        if (rc.getType().equalsIgnoreCase(type) && rc.getPrice() <= price && rc.getVacancy() > 0) {
          matchedRooms.add(room);
          break;
        }
      }
    }
    return matchedRooms;
  }

  public int reserveRoom(int roomNumber, String type, String checkin, String checkout) {
    for (Room room : vacantRooms) {
      if (room.getRoomNumber() == roomNumber) {
        for (RoomClass rc : room.getRoomClasses()) {
          if (rc.getType().equalsIgnoreCase(type) && rc.getVacancy() > 0) {
            rc.decrementVacancy();
            Reservation r = new Reservation(roomNumber, type, checkin, checkout);
            reservations.put(r.getReservationId(), r);
            return r.getReservationId();
          }
        }
      }
    }
    return -1;
  }

  public void checkIn(int reservationId, String type, String checkin, String checkout, int roomNumber) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (r.isCheckedIn()) {
        System.out.println("すでにチェックイン済みです。");
      } else if (r.matches(type, checkin, checkout, roomNumber)) {
        r.setCheckedIn(true);
        System.out.println("チェックイン完了しました。");
      } else {
        System.out.println("入力情報が一致しません。チェックインできません。");
      }
    } else {
      System.out.println("その予約番号は存在しません。");
    }
  }

  public void checkOut(int reservationId) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (!r.isCheckedOut()) {
        r.setCheckedOut(true);
        System.out.println("チェックアウトが完了しました。");
      } else {
        System.out.println("すでにチェックアウト済みです。");
      }
    } else {
      System.out.println("予約番号が見つかりません。");
    }
  }

  public void cancelReservation(int reservationId, String type, String checkin, String checkout, int roomNumber) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (r.matches(type, checkin, checkout, roomNumber)) {
        for (Room room : vacantRooms) {
          if (room.getRoomNumber() == roomNumber) {
            for (RoomClass rc : room.getRoomClasses()) {
              if (rc.getType().equalsIgnoreCase(type)) {
                rc.incrementVacancy();
                break;
              }
            }
          }
        }
        reservations.remove(reservationId);
        System.out.println("予約キャンセルが完了しました。");
      } else {
        System.out.println("入力情報が一致しません。キャンセルできません。");
      }
    } else {
      System.out.println("予約番号が見つかりません。");
    }
  }

  public void addVacantRoom(Room room) {
    vacantRooms.add(room);
  }
}

// ============================
// ManagementControl
// ============================
class ManagementControl {
  private List<Room> rooms;
  public ManagementControl() { this.rooms = new ArrayList<>(); }
  public void registerVacantRoom(String checkinDate, String checkoutDate,
    String singlePrice, String singleVacancy,
    String doublePrice, String doubleVacancy,
    String suitePrice, String suiteVacancy) {
    SingleRoom single = new SingleRoom(singlePrice, singleVacancy);
    DoubleRoom dbl = new DoubleRoom(doublePrice, doubleVacancy);
    SuiteRoom suite = new SuiteRoom(suitePrice, suiteVacancy);
    List<RoomClass> roomClassList = new ArrayList<>();
    roomClassList.add(single); roomClassList.add(dbl); roomClassList.add(suite);
    Room room = new Room(rooms.size() + 1, checkinDate, checkoutDate, roomClassList);
    rooms.add(room);
  }
  public List<Room> getRooms() { return rooms; }
}

// ============================
// Main
// ============================
public class HotelReservationSystem {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ManagementControl management = new ManagementControl();
    ReservationControl reservation = new ReservationControl();

    management.registerVacantRoom("2025/07/20", "2025/07/21", "10000", "3", "15000", "2", "30000", "1");
    management.registerVacantRoom("2025/07/21", "2025/07/22", "12000", "1", "18000", "2", "32000", "1");
    for (Room room : management.getRooms()) {
      reservation.addVacantRoom(room);
    }

    while (true) {
      System.out.println("\n1. 予約\n2. チェックイン\n3. チェックアウト\n4. 予約キャンセル\n5. 終了");
      String input = scanner.nextLine();

      if (input.equals("1")) {
        System.out.println("チェックイン日 (YYYY/MM/DD):");
        String checkin = scanner.nextLine();
        System.out.println("チェックアウト日 (YYYY/MM/DD):");
        String checkout = scanner.nextLine();
        System.out.println("部屋タイプ (Single/Double/Suite):");
        String type = scanner.nextLine();
        System.out.println("価格上限:");
        int price = Integer.parseInt(scanner.nextLine());

        List<Room> candidates = reservation.findVacantRoom(checkin, checkout, price, type);
        if (candidates.isEmpty()) {
          System.out.println("該当する空室がありません。");
        } else {
          System.out.println("予約可能な部屋:");
          for (Room r : candidates) {
            System.out.println("部屋番号: " + r.getRoomNumber());
          }
          System.out.println("予約する部屋番号を入力:");
          int roomNo = Integer.parseInt(scanner.nextLine());
          int resId = reservation.reserveRoom(roomNo, type, checkin, checkout);
          if (resId != -1) {
            System.out.println("予約完了、予約番号: " + resId);
          } else {
            System.out.println("予約に失敗しました。");
          }
        }
      } else if (input.equals("2")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        System.out.println("チェックイン日 (YYYY/MM/DD):");
        String checkin = scanner.nextLine();
        System.out.println("チェックアウト日 (YYYY/MM/DD):");
        String checkout = scanner.nextLine();
        System.out.println("部屋タイプ (Single/Double/Suite):");
        String type = scanner.nextLine();
        System.out.println("部屋番号:");
        int roomNo = Integer.parseInt(scanner.nextLine());
        reservation.checkIn(resId, type, checkin, checkout, roomNo);
      } else if (input.equals("3")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        reservation.checkOut(resId);
      } else if (input.equals("4")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        System.out.println("チェックイン日 (YYYY/MM/DD):");
        String checkin = scanner.nextLine();
        System.out.println("チェックアウト日 (YYYY/MM/DD):");
        String checkout = scanner.nextLine();
        System.out.println("部屋タイプ:");
        String type = scanner.nextLine();
        System.out.println("部屋番号:");
        int roomNo = Integer.parseInt(scanner.nextLine());
        reservation.cancelReservation(resId, type, checkin, checkout, roomNo);
      } else if (input.equals("5")) {
        System.out.println("終了します。");
        break;
      } else {
        System.out.println("無効な選択です。");
      }
    }
  }
}
